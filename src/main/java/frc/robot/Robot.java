// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.Spark;



/** This is a demo program showing how to use Mecanum control with the MecanumDrive class. */
public class Robot extends TimedRobot {
  //CAN and USB IDS
  private static final int kFrontLeftChannel = 4;
  private static final int kRearLeftChannel = 3;
  private static final int kFrontRightChannel = 2;
  private static final int kRearRightChannel = 1;
  private static final int leadDeviceID = 6;
  private static final int followDeviceID = 5;
  //pwm
  private static final int shooterID = 1;
  private static final int PWMSparkIntakeChannel = 0;
  private static final int kJoystickChannel1 = 0;
  private static final int kJoystickChannel2 = 1;
//Arm
  private CANSparkMax m_armLeadMotor;
  //private CANSparkMax m_armFollowMotor;
  private Spark m_intake;
  private Spark m_shooter;

  private MecanumDrive m_robotDrive;
  private XboxController m_stick1;
  private XboxController m_stick2;
  
 

  @Override
  public void robotInit() {
    CANSparkMax frontLeft = new CANSparkMax(kFrontLeftChannel, MotorType.kBrushless);
    CANSparkMax rearLeft = new CANSparkMax(kRearLeftChannel, MotorType.kBrushless);
    CANSparkMax frontRight = new CANSparkMax(kFrontRightChannel, MotorType.kBrushless);
    CANSparkMax rearRight = new CANSparkMax(kRearRightChannel, MotorType.kBrushless);
    m_armLeadMotor = new CANSparkMax(leadDeviceID, MotorType.kBrushless);
   // m_armFollowMotor = new CANSparkMax(followDeviceID, MotorType.kBrushless);
    m_shooter = new Spark(shooterID);
    m_intake = new Spark(PWMSparkIntakeChannel);

 
    
    // Invert the right side motors.
    // You may need to change or remove this to match your robot.
    frontRight.setInverted(true);
    rearRight.setInverted(true);
    //arms
    m_armLeadMotor.restoreFactoryDefaults();
//m_armFollowMotor.restoreFactoryDefaults();
   // m_armFollowMotor.follow(m_armLeadMotor);

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_stick1 = new XboxController(kJoystickChannel1);
    m_stick2  = new XboxController(kJoystickChannel2);

  }

  @Override
  public void teleopPeriodic() {
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
NetworkTableEntry tx = table.getEntry("tx");
NetworkTableEntry ty = table.getEntry("ty");
NetworkTableEntry ta = table.getEntry("ta");

//read values periodically
double x = tx.getDouble(0.0);
double y = ty.getDouble(0.0);
double area = ta.getDouble(0.0);

//post to smart dashboard periodically
SmartDashboard.putNumber("LimelightX", x);
SmartDashboard.putNumber("LimelightY", y);
SmartDashboard.putNumber("LimelightArea", area);


    // Use the joystick Y axis for forward movement, X axis for lateral
    // movement, and Z axis for rotation.
    m_robotDrive.driveCartesian(-m_stick1.getLeftY(), -m_stick1.getLeftX(), -m_stick1.getRightX());
    //should drive two arm motors together?
    m_armLeadMotor.set(m_stick2.getLeftY());
    //if for intake in and out
    if(m_stick2.getAButton()){
      m_intake.set(1);
    }else{
      m_intake.set(0);
    }
    if(m_stick2.getBButton()){
      m_intake.set(-1);
    }else{
      m_intake.set(0);
    }
    //shooter in and out
    if(m_stick2.getYButton()){
      m_shooter.set(1.0);
    }else{
      m_shooter.set(0);
    }
    if(m_stick2.getXButton()){
      m_shooter.set(-1.0);
      }else{
        m_shooter.set(0.0);
      }
    }
  }
  

