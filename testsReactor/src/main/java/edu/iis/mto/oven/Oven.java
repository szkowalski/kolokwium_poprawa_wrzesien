package edu.iis.mto.oven;

public class Oven {

    static final int HEAT_UP_AND_FINISH_SETTING_TIME = 0;
    private final HeatingModule heatingModule;
    private final Fan fan;

    public Oven(HeatingModule heatingModule, Fan fan) {
        this.heatingModule = heatingModule;
        this.fan = fan;
    }

    public void runProgram(BakingProgram program) {
        init(program.getInitialTemp());
        for (ProgramStage programStage : program) {
            runStage(programStage);
        }
        cool(program);
    }

    private void cool(BakingProgram program) {
        if (program.isCoolAtFinish()) {
            fan.on();
        }
    }

    private void init(int initialTemp) {
        if (initialTemp > 0) {
            try {
                heatingModule.heater(HeatingSettings.builder()
                                                    .withTargetTemp(initialTemp)
                                                    .withTimeInMinutes(HEAT_UP_AND_FINISH_SETTING_TIME)
                                                    .build());
            } catch (HeatingException e) {
                throw new OvenException(e);
            }
        }
    }

    private void runStage(ProgramStage programStage) {
        try {
            if (programStage.getHeat() == HeatType.THERMO_CIRCULATION) {
                fan.on();
                heatingModule.termalCircuit(settings(programStage));
                fan.off();
            } else {
                if (fan.isOn()) {
                    fan.off();
                }
                runHeatingProgram(programStage);
            }
        } catch (HeatingException e) {
            throw new OvenException(e);
        }
    }

    private void runHeatingProgram(ProgramStage stage) throws HeatingException {
        HeatingSettings settings = settings(stage);
        if (stage.getHeat() == HeatType.GRILL) {
            heatingModule.grill(settings);
        } else {
            heatingModule.heater(settings);
        }
    }

    private HeatingSettings settings(ProgramStage stage) {
        return HeatingSettings.builder()
                              .withTargetTemp(stage.getTargetTemp())
                              .withTimeInMinutes(stage.getStageTime())
                              .build();
    }
}
