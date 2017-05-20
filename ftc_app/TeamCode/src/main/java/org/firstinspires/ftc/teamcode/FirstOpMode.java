package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.configuration.MotorConfigurationType;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by 1803982879 on 5/13/2017.
 */

@TeleOp(name="My First Op Mode", group="Practice-Bot")

public class FirstOpMode extends LinearOpMode {
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private ElapsedTime period = new ElapsedTime();

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome
     * with a regular periodic tick. This is used to compensate for varying
     * processing times for each cycle. The function looks at the elapsed cycle time,
     * and sleeps for the remaining time interval.
     *
     * @param periodMs Length of wait cycle in mSec.
     */

    private void waitForTick(long periodMs) throws java.lang.InterruptedException
    {
        long remaining = periodMs - (long)period.milliseconds();
        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            Thread.sleep(remaining);
        }
        // Reset the cycle clock for the next pass.
        period.reset();
    }
    @Override
    public void runOpMode() {
        double left = 0.0;
        double right = 0.0;

        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver"); //
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        try {
            // run until the end of the match (driver presses STOP)
            while (opModeIsActive()) {
                // Run wheels in tank mode (note: The joystick goes negative when

                // pushed forwards, so negate it)

                left = -gamepad1.left_stick_y;

                right = -gamepad1.right_stick_y;

                leftMotor.setPower(left);

                rightMotor.setPower(right);

                // Send telemetry message to signify robot running;

                telemetry.addData("left", "%.2f", left);

                telemetry.addData("right", "%.2f", right);

                telemetry.update();

                // Pause for metronome tick. 40 mS each cycle = update 25 times
                // a second.
                waitForTick(40);
            }
        }

        catch (java.lang.InterruptedException exc) {

            return;

        }

        finally {

            leftMotor.setPower(0);

            rightMotor.setPower(0);

        }

    }

}
