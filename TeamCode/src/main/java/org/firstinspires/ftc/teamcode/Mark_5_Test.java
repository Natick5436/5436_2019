package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Mark 5 Test", group = "TeleOp")
public class Mark_5_Test extends LinearOpMode {
    Mark_5 robot = new Mark_5(this);
    final int ARM_OUT = -2520;
    final int ARM_MID = -1260;
    final int ARM_IN = 0;

    final double CLAMP_CLOSE = 0.9;
    final double CLAMP_OPEN = 0.45;

    final double FLIP_COLLECT = 0.33;
    final double FLIP_STORE = 1;

    double drivePower;
    boolean fastMode;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.initialize(hardwareMap, 0, 0, 0);
        drivePower = 0.5;
        fastMode = false;
        waitForStart();

        while (opModeIsActive()) {
            /*if (robot.encoderCount > -1260){
                robot.flip.setPosition(1);
            }
            if (robot.encoderCount < -1260){
                robot.flip.setPosition(0.33);
            }
            if (gamepad2.dpad_left){
                //while(robot.encoderCount < -300){
                    robot.arm.setPower(0.2);
                    if (isStopRequested()){return;}
                }
                robot.arm.setPower(0);

            //}*/

            //Arm controls
            if(gamepad2.a){
                if(Math.abs(robot.arm.getCurrentPosition()-ARM_IN)>robot.armAccuracy){
                    robot.arm.setPower(0.6*Math.abs(robot.arm.getCurrentPosition())/robot.arm.getCurrentPosition());
                }else{
                    robot.arm.setPower(0);
                }
                if(robot.arm.getCurrentPosition() < ARM_MID){
                    robot.flip.setPosition(FLIP_COLLECT);
                }else if(robot.arm.getCurrentPosition() > ARM_MID){
                    robot.flip.setPosition(FLIP_STORE);
                }
            }else if(gamepad2.y){
                if(Math.abs(robot.arm.getCurrentPosition()-ARM_OUT)>robot.armAccuracy){
                    robot.arm.setPower(0.6*Math.abs(robot.arm.getCurrentPosition()-ARM_OUT)/(robot.arm.getCurrentPosition()-ARM_OUT));
                }else{
                    robot.arm.setPower(0);
                }
                if(robot.arm.getCurrentPosition() < ARM_MID){
                    robot.flip.setPosition(FLIP_COLLECT);
                }else if(robot.arm.getCurrentPosition() > ARM_MID){
                    robot.flip.setPosition(FLIP_STORE);
                }
            }else if (gamepad2.dpad_up){
                robot.arm.setPower(0.6);
                robot.encoderCount = robot.arm.getCurrentPosition();
            }else if (gamepad2.dpad_down){
                robot.arm.setPower(-0.6);
                robot.encoderCount = robot.arm.getCurrentPosition();
            }else{
                robot.arm.setPower(0.0);
                robot.arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
            telemetry.addData("arm pos", robot.encoderCount);

            //Manuel flip controls (will not affect automatic if not touching buttons at the same time)
            //e.g. NO x and a at the same time
            if(gamepad2.b && (!gamepad2.a||!gamepad2.y)){
                robot.flip.setPosition(FLIP_STORE);
            }if(gamepad2.x && (!gamepad2.a||!gamepad2.y)){
                robot.flip.setPosition(FLIP_COLLECT);
            }

            //Clamp is a completely manuel function for now
            if (gamepad2.right_bumper){
                robot.clamp.setPosition(CLAMP_CLOSE);
            }
            if (gamepad2.left_bumper){
                robot.clamp.setPosition(CLAMP_OPEN);
            }

            //Lift controls
            robot.liftL.setPower(gamepad2.right_trigger-gamepad2.left_trigger);
            robot.liftR.setPower(gamepad2.right_trigger-gamepad2.left_trigger);

            //Foundation grabber controls
            if(gamepad1.a) {
                robot.grabL.setPosition(1);
                robot.grabR.setPosition(1);
            }else if(gamepad1.y){
                robot.grabL.setPosition(0);
                robot.grabR.setPosition(0);
            }

            //Drive System
            if(gamepad1.left_bumper){
                robot.strafe(-drivePower);
            }else if(gamepad1.right_bumper){
                robot.strafe(drivePower);
            }else if((gamepad1.right_trigger-gamepad1.left_trigger) != 0){
                robot.strafe(drivePower*(gamepad1.right_trigger-gamepad1.left_trigger));
            }else if(gamepad1.dpad_up){
                robot.forward(drivePower);
            }else if(gamepad1.dpad_down){
                robot.forward(-drivePower);
            }else{
                robot.lF.setPower(drivePower*gamepad1.left_stick_y);
                robot.lB.setPower(drivePower*gamepad1.left_stick_y);
                robot.rF.setPower(drivePower*gamepad1.right_stick_y);
                robot.rB.setPower(drivePower*gamepad1.right_stick_y);
                robot.setStatus(Mark_5.Status.DRIVING);
            }

            //Fast mode/slow mode swap
            if(gamepad1.left_stick_button && gamepad1.right_stick_button) {
                fastMode = !fastMode;
            }
            if(fastMode){
                drivePower = 1;
                telemetry.addData("!FAST MODE!", "!HOLD YOUR HORSES! (click both stick buttons to cancel)");
            }else{
                drivePower = 0.5;
                telemetry.addData("You are in Slow mode", "(click both stick buttons to engage fast mode)");
            }

            robot.updateVuforia();
            telemetry.addData("Heading", robot.getHeading());
            telemetry.addData("grabL position", robot.grabL.getPosition());
            telemetry.addData("grabR position", robot.grabR.getPosition());
            telemetry.update();
        }
    }
}

