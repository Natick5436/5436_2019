package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Mark_6;
import org.firstinspires.ftc.teamcode.Hardware.Vuforia;
import org.firstinspires.ftc.teamcode.Math.ACMath;
import org.firstinspires.ftc.teamcode.ThreadsandInterfaces.SKYSTONE;

@Autonomous(name = "M6 Blue Skystone", group = "Autonomous")
public class M6_Blue_Skystone_Auto  extends LinearOpMode {
    Mark_6 robot = new Mark_6(this);
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException{
        Vuforia t1 = new Vuforia(this, hardwareMap);
        t1.start();
        robot.initialize(hardwareMap, 0.22, 0.78, Math.PI/2, false);

        t1.redMode = false;
        int itemSelected = 0;
        String[] settingNames = {"Move foundation(true = yes, false = no): ", "Park skybridge side(true) or wall side (false): "};
        boolean[] settings = new boolean[settingNames.length];
        boolean active = false;
        while(!isStarted() && !isStopRequested()){
            if(gamepad1.dpad_up && !active){
                if(itemSelected <= 0){
                    itemSelected = settingNames.length-1;
                }else{
                    itemSelected--;
                }
                active = true;
            }else if(gamepad1.dpad_down && !active){
                if(itemSelected >= settingNames.length-1){
                    itemSelected = 0;
                }else{
                    itemSelected++;
                }
                active = true;
            }else if((gamepad1.dpad_left|| gamepad1.dpad_right)&& !active){
                settings[itemSelected] = !settings[itemSelected];
                active = true;
            }else if(active&&!gamepad1.dpad_right&&!gamepad1.dpad_left&&!gamepad1.dpad_up&&!gamepad1.dpad_down){
                active = false;
            }
            if(!gamepad1.a) {
                for (int i = 0; i < settingNames.length; i++) {
                    if (i == itemSelected) {
                        telemetry.addData(">>>" + settingNames[i], settings[i]);
                    } else {
                        telemetry.addData(settingNames[i], settings[i]);
                    }
                }
                telemetry.update();
            }
        }
        robot.odo.start();
        runtime.reset();
        waitForStart();
        // t1.exit = true;
        // Thread.sleep(600);
        // t1.exit = false;
        // robot.forward(0.5, 0.01, true);
        // sleep(1000);
        int finalPos = 2;
        if (t1.pos != 2){
            robot.forward(1.0,0.05,true);
            sleep(1000);
            if (t1.isSkystone()){
                finalPos = 3;
            }else{
                finalPos = 1;
            }
        }
        telemetry.addData("Stone pos", finalPos);
        telemetry.update();
        sleep(2000);
        robot.strafe(1.0,SKYSTONE.DISTANCE_TO_STONES/2-SKYSTONE.ROBOT_WIDTH/2,true);
        robot.turn(0.3, 0,false);
        if (finalPos == 1) {
            robot.strafe(0.6, 0.5, true);
        }
        robot.forward(0.2,((SKYSTONE.DISTANCE_TO_STONES/2)-SKYSTONE.ROBOT_WIDTH/2-0.27),true);
        robot.skyArm1.setPosition(0.85);
        sleep(500);
        robot.skyClamp2.setPosition(0.5);
        sleep(200);
        robot.skyArm2.setPosition(0.45);
        robot.forward(0.5,-0.05,true);
        robot.goToAbsolutePosition(1,0.6, robot.odo.getX(),SKYSTONE.FIELD_WIDTH-SKYSTONE.WALL_TO_FOUNDATION-SKYSTONE.FOUNDATION_LENGTH/2-0.07);
        robot.turn(0.6,0,false);
        robot.forward(0.5, 0.18, false);
        robot.skyArm2.setPosition(0.7);
        sleep(500);
        robot.skyClamp2.setPosition(0.9);
        sleep(200);
        robot.skyArm1.setPosition(0.45);
    }
}
