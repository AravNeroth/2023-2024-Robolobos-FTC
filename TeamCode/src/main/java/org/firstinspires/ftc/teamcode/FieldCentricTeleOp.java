package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
//import org.firstinspires.ftc.teamcode.commands.VoltageReader;
import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.ControllerFeatures;
import org.firstinspires.ftc.teamcode.subsystems.Turrent;
import org.firstinspires.ftc.teamcode.subsystems.Wheels;

/*
    The reason why this class has OpMode instead of LinearOpMode is because
    there are different methods in the OP mode, such as the loop() method.
    Since this method pulls from the Wheels subsystem, it requires a loop
    to constantly update the IMU inside the subsystem. In LinearOpMode, the
    equivalent would be to update the IMU in the "opModeIsActive" method
 */
@TeleOp(name="FieldCentricTeleOp", group="DriveModes")
public abstract class FieldCentricTeleOp extends OpMode {

    double mult = 0.70;
    // sets controller colors- find in Subsystem ControllerLights
    private Wheels wheels;
    private Arm arm;
    private Claw claw;
    private Turrent turret;
    private ControllerFeatures features;
    
    private GamepadEx pilot, sentry;
    private ElapsedTime runTime;
    @Override
    public void init(){

        telemetry.addLine("Initializatizing Robot");
        telemetry.update();


        pilot = new GamepadEx(gamepad1);
        sentry = new GamepadEx(gamepad2);

        wheels = new Wheels();
        arm = new Arm();
        turret = new Turrent();
        features = new ControllerFeatures();
        runTime = new ElapsedTime();

        /*
            when entering in gamepad1 & gamepad 2, it should be used for LED and Rumble only!
            For getting DATA use the GamepadEx class from FTClib
            https://docs.ftclib.org/ftclib/features/gamepad-extensions

            for some reason GamepadEx doesn't play nice with Gamepad, even though its a wrapper class
         */

        features.rumbleOnStart(gamepad1, gamepad2);
        features.setPink(gamepad1, gamepad2, 120);

        telemetry.addLine("ALl Subsystems & Controllers Actvated");
        telemetry.addLine("Initialization Completed Successfully.");
        telemetry.addLine("Time taken: " + getRuntime()+ " seconds.");
        telemetry.update();
    }

    @Override
    public void loop() {
        pilot.readButtons();
        sentry.readButtons();

        // multplier for the wheels- currently running @ 70%
        wheels.fieldCentric(pilot);

        // color
        features.setPurple(gamepad1, gamepad2, 100000);


        // speeding controls
        // if the trigger is pressed halfway, then it'll boost

            wheels.setMult(features.leftTriggerBoost(pilot));

        // if the trigger is pressed halfway, then it'll slow

            wheels.setMult(features.rightTriggerSlow(pilot));


            // IMU reset
        if(pilot.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
            wheels.resetIMU();
            features.lightRumble(gamepad1, 100);
        }

        // turret controls
        if (sentry.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT))
            turret.turnRight();


        if (sentry.wasJustPressed(GamepadKeys.Button.DPAD_LEFT))
            turret.turnLeft();

    }

    @Override
    public void stop()
    {
        telemetry.addLine("Robot Shut Down.");
        telemetry.addLine("Total Runtime: " + runTime + " seconds.");
        telemetry.update();
    }


}