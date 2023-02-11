package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class DozerTwoStick extends LinearOpMode {

    private CRServo backLeft;
    private CRServo backRight;
    private Servo eyes;
    private Servo plowLeft;
    private Servo plowRight;

    @Override
    public void runOpMode() {
        double y;
        double clockwise;
        double bl;
        double br;
        int speed = 1;
        boolean shouldSlowmode = false;
        boolean slowmodeChanged = false;
        double plowPos = 0.1;
        boolean eyesOnTurn = true;
        boolean eyesChanged = false;

        backLeft = hardwareMap.get(CRServo.class, "backLeft");
        backRight = hardwareMap.get(CRServo.class, "backRight");
        eyes = hardwareMap.get(Servo.class, "eyes");
        plowLeft = hardwareMap.get(Servo.class, "plowLeft");
        plowRight = hardwareMap.get(Servo.class, "plowRight");

        plowLeft.setDirection(Servo.Direction.REVERSE);

        eyes.setPosition(0.5);
        plowLeft.setPosition(plowPos);
        plowRight.setPosition(plowPos);

        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                backLeft.setPower(gamepad1.left_stick_y);
                backRight.setPower(-gamepad1.right_stick_y);

                if (gamepad1.back) {
                    if (!eyesChanged) {
                        eyesOnTurn = !eyesOnTurn;
                        eyesChanged = true;
                    }
                } else if (eyesChanged){
                    eyesChanged = false;
                }

                if (eyesOnTurn) {
                    eyes.setPosition(0.3 * -(gamepad1.left_stick_y - gamepad1.right_stick_y) + 0.5);
                } else if ((gamepad1.right_trigger - gamepad1.left_trigger) != 0) {
                    if (eyes.getPosition() < 0.2) {
                        eyes.setPosition(0.2);
                    } else if (eyes.getPosition() > 0.8) {
                        eyes.setPosition(0.8);
                    } else {
                        eyes.setPosition(eyes.getPosition() + 0.01 * (gamepad1.right_trigger - gamepad1.left_trigger));
                        sleep(8);
                    }
                }

                if (plowPos < 0.075) {
                    plowPos = 0.075;
                } else if (plowPos > 0.6) {
                    plowPos = 0.6;
                } else {
                    plowPos += 0.01 * ((gamepad1.dpad_up ? 1 : 0) - (gamepad1.dpad_down ? 1 : 0));
                    sleep(8);
                }

                plowLeft.setPosition(plowPos);
                plowRight.setPosition(plowPos);

                telemetry.update();
            }
        }
    }
}