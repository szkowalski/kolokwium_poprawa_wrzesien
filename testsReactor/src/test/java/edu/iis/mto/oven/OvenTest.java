package edu.iis.mto.oven;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
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
                    .withStageTime(60)
                    .withHeat(HeatType.THERMO_CIRCULATION)
                    .withTargetTemp(220)
                    .build(),
            ProgramStage.builder()
                    .withStageTime(120)
                    .withHeat(HeatType.GRILL)
                    .withTargetTemp(220)
                    .build()
    );
    BakingProgram bakingProgram;

    int properTemp=150;

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
    void shouldInvokeGrillWhenProgramHasGrillHeatType()
    {
        fail("unimplemented");
    }

    @Test
    void shouldThrowOvenExceptionOnHeatingException()
    {
        fail("unimplemented");
    }

}
