package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by 1803982879 on 20.5.2017.
 */

//@TeleOp(name="My Second Op Mode", group="Practice-Bot")


public class SecondOpMode extends LinearOpMode {
    private DcMotor leftMotor1;
    private DcMotor rightMotor1;
    private DcMotor leftMotor2;
    private DcMotor rightMotor2;
    private DcMotor ballCollectorMotor;
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

        leftMotor1 = hardwareMap.dcMotor.get("left1");
        leftMotor2 = hardwareMap.dcMotor.get("left2");
        rightMotor1 = hardwareMap.dcMotor.get("right1");
        rightMotor2 = hardwareMap.dcMotor.get("right2");
        ballCollectorMotor = hardwareMap.dcMotor.get("BallCollector");
        rightMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
        // Set all motors to zero power
        leftMotor1.setPower(0);
        leftMotor2.setPower(0);
        rightMotor1.setPower(0);
        rightMotor2.setPower(0);

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
                left = Math.sqrt(Math.abs(gamepad1.left_stick_y) * 100) / 10;
                if (gamepad1.left_stick_y < 0)
                {
                    left = -left;
                }

                right = Math.sqrt(Math.abs(gamepad1.right_stick_y) * 100) / 10;
                if (gamepad1.right_stick_y < 0)
                {
                    right = -right;
                }

                leftMotor1.setPower(left);
                leftMotor2.setPower(left);

                rightMotor1.setPower(right);
                rightMotor2.setPower(right);

                if (gamepad1.b == true)
                {
                    ballCollectorMotor.setPower(-0.8);
                    telemetry.addLine("B button on");
                }
                else {
                    ballCollectorMotor.setPower(0);
                    telemetry.addLine("B button off");
                }

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
            leftMotor1.setPower(0);
            leftMotor2.setPower(0);

            rightMotor1.setPower(0);
            rightMotor2.setPower(0);

            ballCollectorMotor.setPower(0);

        }

    }

}
