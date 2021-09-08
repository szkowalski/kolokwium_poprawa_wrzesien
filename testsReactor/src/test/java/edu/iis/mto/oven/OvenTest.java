package edu.iis.mto.oven;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @BeforeEach
    void setUp() {
        oven = new Oven(heatingModule, ovenFan);
    }

    @Test
    void shouldStartBakingWithProperTemp()
    {
        int initialTemp = 120;

        bakingProgram = BakingProgram.builder()
                .withInitialTemp(initialTemp)
                .withStages(programStages)
                .build();

        oven.runProgram(bakingProgram);

        assertEquals(initialTemp, bakingProgram.getInitialTemp());
    }

    @Test
    void shouldInvokeFanMethodWhenProgramWithThermalCircuit()
    {
        fail("unimplemented");
    }

    @Test
    void shouldOnAndOffFanWhenProgramWithThermalCircuit()
    {
        fail("unimplemented");
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
