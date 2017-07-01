package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by 1803982879 on 1.7.2017.
 */


@TeleOp(name="Prime Operation Mode", group="Ice-bot 3.0")


public class PrimeOpMode extends LinearOpMode {
    // Initialization
    private DcMotor leftDriveMotor1;
    private DcMotor rightDriveMotor1;
    private DcMotor leftDriveMotor2;
    private DcMotor rightDriveMotor2;
    private DcMotor elevatorMotor;
    private DcMotor ballCollectorMotor;

    private DcMotor leftHookMotor; //Should not be used in this OP mode
    private DcMotor rightHookMotor;  // Should not be used in this OP mode

    private Servo sorterServo; //Will have manual and automatic modes.
    private Servo blueDoorServo;
    private Servo orangeDoorServo;

    NormalizedColorSensor colorSensor;


    static final int    CYCLE_MS    =   20;     // period of each cycle



    //Main Loop
    @Override
    public void runOpMode() throws InterruptedException {

        //Init
        leftDriveMotor1 = hardwareMap.dcMotor.get("leftDriveMotor1");
        leftDriveMotor2 = hardwareMap.dcMotor.get("leftDriveMotor2");
        rightDriveMotor1 = hardwareMap.dcMotor.get("rightDriveMotor1");
        rightDriveMotor2 = hardwareMap.dcMotor.get("rightDriveMotor2");
        elevatorMotor = hardwareMap.dcMotor.get("elevatorMotor");
        ballCollectorMotor = hardwareMap.dcMotor.get("ballCollectorMotor");

        leftHookMotor = hardwareMap.dcMotor.get("leftHookMotor");
        rightHookMotor = hardwareMap.dcMotor.get("rightHookMotor");


        sorterServo = hardwareMap.servo.get("sorterServo");
        sorterServo = hardwareMap.servo.get("blueDoorServo");
        sorterServo = hardwareMap.servo.get("orangeDoorServo");
        sorterServo.scaleRange(0.0, 1.0);

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");

        double sorterPosition = 0.5;

        sorterServo.setPosition(sorterPosition);



        ballCollectorMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        elevatorMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        telemetry.addData(">", "Start to begin." );
        telemetry.update();
        waitForStart();

        try {
            while (opModeIsActive()) {
                telemetry.clearAll();

                //Drive train

                double left = 0.0;
                double right = 0.0;

                left = Math.sqrt(Math.abs(gamepad1.left_stick_y) * 100) / 12;
                if (gamepad1.left_stick_y < 0) {
                    left = -left;
                }

                right = Math.sqrt(Math.abs(gamepad1.right_stick_y) * 100) / 12;
                if (gamepad1.right_stick_y < 0) {
                    right = -right;
                }

                if (gamepad1.right_bumper == true) {
                    left = right;
                }

                leftDriveMotor1.setPower(left);
                leftDriveMotor2.setPower(left);

                rightDriveMotor1.setPower(-right);
                rightDriveMotor2.setPower(-right);


                if (gamepad1.left_trigger > 0.1) {
                    leftHookMotor.setPower(-gamepad1.left_trigger);
                    rightHookMotor.setPower(-gamepad1.left_trigger);
                } else if (gamepad1.right_trigger > 0.1) {
                    leftHookMotor.setPower(0.2);
                    rightHookMotor.setPower(0.2);
                } else {
                    leftHookMotor.setPower(0);
                    rightHookMotor.setPower(0);
                }


                //Inner workings


                if (gamepad2.a) {
                    ballCollectorMotor.setPower(0);
                } else {
                    ballCollectorMotor.setPower(0.5);
                }

                if (gamepad2.b)
                {
                    elevatorMotor.setPower(0);
                }
                else{
                    elevatorMotor.setPower(0.8);
                }


                //Color Sensor and sorter
                if (gamepad1.x == true) {
                    sorterPosition = 0.35;
                } else if (gamepad1.y == true) {
                    sorterPosition = 0.35;
                }
                else if (gamepad1.b == true)
                {
                    sorterPosition = 0.5;
                }

                NormalizedRGBA colors = colorSensor.getNormalizedColors();

                /** Use telemetry to display feedback on the driver station. We show the conversion
                 * of the colors to hue, saturation and value, and display the the normalized values
                 * as returned from the sensor.
                 * @see <a href="http://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html">HSV</a>*/
                float[] hsvValues = new float[3];
                Color.colorToHSV(colors.toColor(), hsvValues);
                telemetry.addLine()
                        .addData("H", "%.3f", hsvValues[0])
                        .addData("S", "%.3f", hsvValues[1])
                        .addData("V", "%.3f", hsvValues[2]);
                telemetry.addLine()
                        .addData("a", "%.3f", colors.alpha)
                        .addData("r", "%.3f", colors.red)
                        .addData("g", "%.3f", colors.green)
                        .addData("b", "%.3f", colors.blue);




                /** We also display a conversion of the colors to an equivalent Android color integer.
                 * @see Color */
                @ColorInt int color = colors.toColor();
                telemetry.addLine("color: ")
                        .addData("a", "%02x", Color.alpha(color))
                        .addData("r", "%02x", Color.red(color))
                        .addData("g", "%02x", Color.green(color))
                        .addData("b", "%02x", Color.blue(color));

                if (colors.red > 0.02 && colors.blue < 0.04 && colors.red > colors.blue * 2)
                {
                    telemetry.addLine("ORANGE");
                    sorterPosition = 0.65;
                }
                else if (colors.red < 0.04 && colors.blue > 0.02 && colors.red * 2 < colors.blue)
                {
                    telemetry.addLine("BLUE");
                    sorterPosition = 0.35;
                }

                sorterServo.setPosition(sorterPosition);


                telemetry.addData("Sorter: ", sorterServo.getPosition());
                telemetry.update();

                sleep(CYCLE_MS);
                idle();
            }
        }
        finally {
            leftDriveMotor1.setPower(0);
            leftDriveMotor2.setPower(0);
            rightDriveMotor1.setPower(0);
            rightDriveMotor2.setPower(0);
            elevatorMotor.setPower(0);
            ballCollectorMotor.setPower(0);
            leftHookMotor.setPower(0);
            rightHookMotor.setPower(0);

        }
    }

    //Exit

}
