package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.qualcomm.ftccommon.UpdateUI;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.robotcore.internal.GamepadUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    private NormalizedColorSensor colorSensor;

    private boolean[] lastGamepad1;
    private boolean[] lastGamepad2;


    static final int    CYCLE_MS    =   20;     // period of each cycle

    Queue<String> eventQueue = new LinkedList<String>();



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

        List<DcMotor> allMotors = new ArrayList<>();

        allMotors.add(leftDriveMotor1);
        allMotors.add(leftDriveMotor2);
        allMotors.add(rightDriveMotor1);
        allMotors.add(rightDriveMotor2);
        allMotors.add(elevatorMotor);
        allMotors.add(ballCollectorMotor);
        allMotors.add(leftHookMotor);
        allMotors.add(rightHookMotor);


        sorterServo = hardwareMap.servo.get("sorterServo");
        orangeDoorServo = hardwareMap.servo.get("orangeDoorServo");
        orangeDoorServo.setDirection(Servo.Direction.REVERSE);
        blueDoorServo = hardwareMap.servo.get("blueDoorServo");
        sorterServo.scaleRange(0.0, 1.0);

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");

        double sorterPosition = 0.5;
        double orangeDoorPosition = 0;
        double blueDoorPosition = 0;

        int blueCount = 0;
        int orangeCount = 0;
        boolean ball = false;

        blueDoorServo.setPosition(orangeDoorPosition);
        orangeDoorServo.setPosition(blueDoorPosition);

        sorterServo.setPosition(sorterPosition);

        ballCollectorMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        elevatorMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData(">", "Start to begin." );
        telemetry.update();
        waitForStart();

        ballCollectorMotor.setPower(0.7);
        elevatorMotor.setPower(0.83);

        try {
            while (opModeIsActive()) {
                telemetry.clearAll();

                List<DcMotor> leftMotors = new ArrayList<>();
                
                leftMotors.add(leftDriveMotor1);
                leftMotors.add(leftDriveMotor2);

                List<DcMotor> rightMotors = new ArrayList<>();

                rightMotors.add(rightDriveMotor1);
                rightMotors.add(rightDriveMotor2);


                RobotFunctions.driveByGamepad(gamepad1, leftMotors, rightMotors);

                //Hooks


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


                if (gamepad2.b) {
                    ballCollectorMotor.setPower(0);
                }
                if (gamepad2.y)
                {
                    elevatorMotor.setPower(0);
                }
                if (gamepad2.a) {
                    ballCollectorMotor.setPower(0.7);
                }
                if (gamepad2.x)
                {
                    elevatorMotor.setPower(0.83);
                }

                //Color Sensor and sorter
                if (gamepad1.x == true) {
                    sorterPosition = 0.35;
                } else if (gamepad1.y == true) {
                    sorterPosition = 0.60;
                }
                else if (gamepad1.b == true)
                {
                    sorterPosition = 0.5;
                }

                int ballColor = RobotFunctions.senseBallColor(colorSensor);

                if (ballColor == 2)
                {
                    telemetry.addLine("Ball color: ORANGE");
                    if (ball == false){
                        orangeCount++;
                    }
                    sorterPosition = 0.60;
                    ball = true;
                }
                else if (ballColor == 1)
                {
                    telemetry.addLine("Ball color: BLUE");
                    if (ball == false){
                        blueCount++;
                    }
                    sorterPosition = 0.35;
                    ball = true;
                }
                else{
                    telemetry.addLine("Ball color: No color detected");
                    ball = false;
                }


                sorterServo.setPosition(sorterPosition);


                //Ball doors
                if (gamepad2.dpad_left){
                    blueDoorPosition = 0.5;
                    blueCount = 0;
                }
                else{
                    blueDoorPosition = 0;
                }
                if (gamepad2.dpad_right){
                    orangeDoorPosition = 0.5;
                    orangeCount = 0;
                }
                else{
                    orangeDoorPosition = 0;
                }


                blueDoorServo.setPosition(orangeDoorPosition);
                orangeDoorServo.setPosition(blueDoorPosition);



                telemetry.addData("Sorter: ", sorterServo.getPosition());
                telemetry.addLine(RobotFunctions.getMotorTelemetryString(allMotors, hardwareMap));
                telemetry.addData("Blue ball count:", blueCount);
                telemetry.addData("Orange ball count:", orangeCount);
                telemetry.update();

                sleep(CYCLE_MS);
            }
        }
        catch (Exception ex) {
            telemetry.addData("Exception: ", ex);
            telemetry.update();
            return;
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
            sorterServo.setPosition(0.5);
        }
    }
    //Exit

}