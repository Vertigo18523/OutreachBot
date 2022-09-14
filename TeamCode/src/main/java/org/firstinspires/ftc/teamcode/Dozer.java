package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class Dozer extends LinearOpMode {

    private CRServo backLeft;
    private CRServo backRight;
    private Servo eyes;
    private Servo plowLeft;
    private Servo plowRight;

    @Override
    public void runOpMode() {
        float y;
        float clockwise;
        double bl;
        double br;
        int speed = 1;
        boolean slowmodeChanged = false;
        boolean shouldSlowmode = false;
        double plowPos = 0.1;

        backLeft = hardwareMap.get(CRServo.class, "backLeft");
        backRight = hardwareMap.get(CRServo.class, "backRight");
        eyes = hardwareMap.get(Servo.class, "eyes");
        plowLeft = hardwareMap.get(Servo.class, "plowLeft");
        plowRight = hardwareMap.get(Servo.class, "plowRight");

        backLeft.setDirection(CRServo.Direction.REVERSE);
        plowLeft.setDirection(Servo.Direction.REVERSE);

        eyes.setPosition(0.5);
        plowLeft.setPosition(plowPos);
        plowRight.setPosition(plowPos);

        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                clockwise = gamepad1.left_stick_x;
                y = -gamepad1.left_stick_y;

                if (gamepad1.dpad_up) {
                    y = (float) 1.0;
                } else if (gamepad1.dpad_down) {
                    y = (float) -1.0;
                }

                if (gamepad1.dpad_right) {
                    clockwise = (float) 1.0;
                } else if (gamepad1.dpad_left) {
                    clockwise = (float) -1.0;
                }

                if (gamepad1.back) {
                    clockwise = (float) -1.0;
                } else if (gamepad1.guide) {
                    clockwise = (float) 1.0;
                }

                bl = y + clockwise;
                br = y - clockwise;

                if (gamepad1.right_bumper) {
                    speed = 2;
                } else if (gamepad1.left_bumper) {
                    speed = 4;
                } else if (gamepad1.start) {
                    if (!slowmodeChanged) {
                        shouldSlowmode = !shouldSlowmode;
                        slowmodeChanged = true;
                    }
                } else {
                    if (slowmodeChanged) {
                        slowmodeChanged = false;
                    } else {
                        speed = 1;
                    }
                }
                if (shouldSlowmode) {
                    speed = 2;
                }

                bl /= speed;
                br /= speed;

                backLeft.setPower(bl);
                backRight.setPower(br);

                if (gamepad1.right_stick_x != 0) {
                    if (eyes.getPosition() < 0.2) {
                        eyes.setPosition((double) 0.2);
                    } else if (eyes.getPosition() > 0.8) {
                        eyes.setPosition((double) 0.8);
                    } else {
                        eyes.setPosition(eyes.getPosition() + 0.01 * gamepad1.right_stick_x);
                        sleep(8);
                    }
                }

                if (gamepad1.right_stick_y != 0) {
                    if (plowPos > 0.6) {
                        plowPos = 0.6;
                    } else {
                        plowPos += 0.01 * -gamepad1.right_stick_y;
                        sleep(8);
                    }
                }
                plowLeft.setPosition(plowPos);
                plowRight.setPosition(plowPos);

                telemetry.update();
            }
        }
    }
}