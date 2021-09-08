package edu.iis.mto.oven;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class OvenTest {

    @Mock
    Fan ovenFan;

    @Mock
    HeatingModule heatingModule;

    Oven oven;

    List<ProgramStage> programStages= List.of(
            ProgramStage.builder()
                    .withStageTime(90)
                    .withHeat(HeatType.THERMO_CIRCULATION)
                    .withTargetTemp(220)
                    .build(),
            ProgramStage.builder()
                    .withStageTime(90)
                    .withHeat(HeatType.GRILL)
                    .withTargetTemp(220)
                    .build()
    );
    BakingProgram bakingProgram;

    int properTemp=150;
    int properTime=90;

    @BeforeEach
    void setUp() {
        oven = new Oven(heatingModule, ovenFan);

        bakingProgram = BakingProgram.builder()
                .withInitialTemp(properTemp)
                .withStages(programStages)
                .build();
    }

    @Test
    void shouldStartBakingWithProperTemp()
    {
        oven.runProgram(bakingProgram);
        assertEquals(properTemp, bakingProgram.getInitialTemp());
    }

    @Test
    void shouldInvokeFanMethodWhenProgramWithThermalCircuit()
    {
        oven.runProgram(bakingProgram);
        verify(ovenFan, times(1)).on();
    }

    @Test
    void shouldOnAndOffFanWhenProgramWithThermalCircuit()
    {
        oven.runProgram(bakingProgram);
        verify(ovenFan, times(1)).off();
        InOrder in = inOrder(ovenFan);
        in.verify(ovenFan).on();
        in.verify(ovenFan).off();
    }

    @Test
    void shouldInvokeGrillWhenProgramHasGrillHeatType() throws HeatingException {


        ProgramStage stage = ProgramStage.builder()
                .withTargetTemp(properTemp)
                .withStageTime(properTime)
                .withHeat(HeatType.GRILL)
                .build();
        List<ProgramStage> stages = List.of(stage);

        HeatingSettings settings = HeatingSettings.builder()
                .withTargetTemp(properTemp)
                .withTimeInMinutes(properTime)
                .build();

        BakingProgram program = BakingProgram.builder()
                .withInitialTemp(0)
                .withStages(stages)
                .build();

        oven.runProgram(program);

        verify(heatingModule).grill(settings);
    }

    @Test
    void shouldThrowOvenExceptionOnHeatingException() throws HeatingException {
        assertNotNull(programStages);
        doThrow(new HeatingException()).when(heatingModule).termalCircuit(any());
        assertThrows(OvenException.class,()->{
            oven.runProgram(bakingProgram);
        });
    }

    @Test
    void shouldInvokeAllTypesInOrderDuringProgramWithDifferentStages() throws HeatingException {
        List<ProgramStage> stages = List.of(
                ProgramStage.builder()
                        .withStageTime(properTime)
                        .withHeat(HeatType.THERMO_CIRCULATION)
                        .withTargetTemp(properTemp)
                        .build(),
                ProgramStage.builder()
                        .withStageTime(properTime)
                        .withHeat(HeatType.GRILL)
                        .withTargetTemp(properTemp)
                        .build(),
                ProgramStage.builder()
                        .withStageTime(properTime)
                        .withHeat(HeatType.HEATER)
                        .withTargetTemp(properTemp)
                        .build()

        );
        bakingProgram = BakingProgram.builder()
                .withInitialTemp(properTemp)
                .withStages(stages)
                .build();
        oven.runProgram(bakingProgram);
        InOrder in = inOrder(heatingModule);
        in.verify(heatingModule).termalCircuit(any(HeatingSettings.class));
        in.verify(heatingModule).grill(any(HeatingSettings.class));
        in.verify(heatingModule).heater(any(HeatingSettings.class));
    }

    @Test
    void shouldInvokeOnceCoolerAtFinishOfProgramWithoutThermoCirculation()
    {
        List<ProgramStage> programStages= List.of(
                ProgramStage.builder()
                        .withStageTime(90)
                        .withHeat(HeatType.HEATER)
                        .withTargetTemp(220)
                        .build(),
                ProgramStage.builder()
                        .withStageTime(90)
                        .withHeat(HeatType.GRILL)
                        .withTargetTemp(220)
                        .build()
        );
        bakingProgram = BakingProgram.builder()
                .withStages(programStages)
                .withInitialTemp(properTemp)
                .withCoolAtFinish(true)
                .build();

        when(ovenFan.isOn()).thenReturn(true);

        oven.runProgram(bakingProgram);

        verify(ovenFan, times(1)).on();
    }

}
