package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import java.util.List;

/**
 * Created by 1803982879 on 8.7.2017.
 */

public class RobotFunctions {

    //Returns if balls are blue, orange or neither

    static int senseBallColor(NormalizedColorSensor colorSensor) throws InterruptedException {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        /** Use telemetry to display feedback on the driver station. We show the conversion
         * of the colors to hue, saturation and value, and display the the normalized values
         * as returned from the sensor.
         * @see <a href="http://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html">HSV</a>*/
        float[] hsvValues = new float[3];
        Color.colorToHSV(colors.toColor(), hsvValues);

        /** We also display a conversion of the colors to an equivalent Android color integer.
         * @see Color */
        @ColorInt int color = colors.toColor();

        if (colors.red > 0.02 && colors.blue < 0.04 && colors.red > colors.blue * 2)
        {
            return 2;
        }
        else if (colors.red < 0.04 && colors.blue > 0.02 && colors.red * 2 < colors.blue)
        {
            return 1;
        }
        return 0;
    }


    //Control given motors with the given gamepad's control sticks. Optimized for navigation.

    static void driveByGamepad(Gamepad g, List<DcMotor> leftMotors, List<DcMotor> rightMotors){
        driveByGamepad(g, leftMotors, rightMotors, false);
    }
    static void driveByGamepad(Gamepad g, List<DcMotor> leftMotors, List<DcMotor> rightMotors, boolean precisionMode){
        double left = 0.0;
        double right = 0.0;

        left = Math.sqrt(Math.abs(g.left_stick_y) * 100) / 12;
        if (precisionMode){
            left = Math.sqrt(Math.abs(g.left_stick_y * 0.4) * 100) / 12;
        }

        if (g.left_stick_y < 0) {
            left = -left;
        }

        right = Math.sqrt(Math.abs(g.right_stick_y) * 100) / 12;
        if (precisionMode){
            right = Math.sqrt(Math.abs(g.right_stick_y * 0.4) * 100) / 12;
        }

        if (g.right_stick_y < 0) {
            right = -right;
        }

        if (g.right_bumper == true) {
            right = left;
        }

        if (g.left_bumper == true){
            right = -left;
        }

        for (DcMotor motor: leftMotors) {
            motor.setPower(left);
        }
        for (DcMotor motor: rightMotors) {
            motor.setPower(-right);
        }
    }

    //Returns a single string detailing all given motors. Each motor's description is broken up with a newline.

    static String getMotorTelemetryString(List<DcMotor> motors, HardwareMap hardwareMap){
        String s = "Motors: ";

        for (DcMotor m: motors
             ) {
            s += hardwareMap.getNamesOf(m) + " Power: " + Math.round(10 * m.getPower()) / 10 + " - port " + m.getPortNumber() + ", \n";
            
        }
        return s;
    }

}
