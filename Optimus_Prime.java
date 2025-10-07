import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.State;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Optimus_Prime")
public class Optimus_Prime extends LinearOpMode {
    
    DcMotorEx m1, m2, m3, m4;
    
    @Override
    public void runOpMode(){
        
        
        // Wheels, ordered accordingly:
        // Top Left, Back Left, Front Right, Back Right
        m1 = hardwareMap.get(DcMotorEx.class, "bl"); //back left
        m2 = hardwareMap.get(DcMotorEx.class, "br"); //back right
        m3 = hardwareMap.get(DcMotorEx.class, "fr"); //front right
        m4 = hardwareMap.get(DcMotorEx.class, "fl"); //front left
    
        //Motor direction, flip to reverse direction of robot (may need to reorder motors)
        //m1.setDirection(DcMotor.Direction.REVERSE);
        m2.setDirection(DcMotor.Direction.REVERSE);
        m3.setDirection(DcMotor.Direction.REVERSE);
        m4.setDirection(DcMotor.Direction.REVERSE);
        
        
        m1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m4.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //Reset the encoders on Init, and set them to the correct mode 
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        telemetry.addData("Press Start When Ready","");
        telemetry.update();
        
        boolean locked = false;
        boolean stealthMode = false;
        
        boolean isLogged = false;
        
      
        waitForStart();
        

        while (opModeIsActive()) {

                // BODY MOVEMENT
            double px = -gamepad1.left_stick_x; //The power of the left stick on the x axis
                                                // Left to Right, -1.00 to 1.00
            double py = gamepad1.left_stick_y; //The power of the left stick on the y axis
                                                // Down to Up, -1.00 to 1.00
            double pa = gamepad1.left_trigger - gamepad1.right_trigger;
                                                //The power of the body rotation

            //math equation stuff...
            
            //p1 correlates to m1
            //p2 correlates to m2
            // etc.
            double p1 = -px + py - pa; //bl
            double p2 = px + py + pa; //br
            double p3 = -px + py + pa; //fr
            double p4 = px + py - pa; //fl
            double max = Math.max(1.0, Math.abs(p1));
            max = Math.max(max, Math.abs(p2));
            max = Math.max(max, Math.abs(p3));
            max = Math.max(max, Math.abs(p4));
            p1 /= max;
            p2 /= max;
            p3 /= max;
            p4 /= max;
            
            
            //Stealth Mode
            if(gamepad1.guide && isLogged == false){
                if(stealthMode == false) stealthMode = true;
                else if(stealthMode == true) stealthMode = false;
                isLogged = true;
                
            }else if (gamepad1.guide != true){
                isLogged = false;
            }
            
            if(stealthMode == true){
                m1.setPower(p1 * 0.1);
                m2.setPower(p2 * 0.1);
                m3.setPower(p3 * 0.1);
                m4.setPower(p4 * 0.1);
            }else {
                m1.setPower(p1 * 0.7);
                m2.setPower(p2 * 0.7); 
                m3.setPower(p3 * 0.7);
                m4.setPower(p4 * 0.7);
            }
            
            //Display Positions of the motors
            telemetry.addData("Motor Encoders"," %d %d %d %d", m1.getCurrentPosition(), m2.getCurrentPosition(),
                    m3.getCurrentPosition(), m4.getCurrentPosition());
            
            
            //telemetry.addData("controller rotate: ", controllerRotation);
            
            telemetry.update();
        }
        //Stops all motors
        m1.setPower(0);
        m2.setPower(0);
        m3.setPower(0);
        m4.setPower(0);
    }
}
