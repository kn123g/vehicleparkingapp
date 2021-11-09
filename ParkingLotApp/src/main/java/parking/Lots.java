package parking;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class Lots {  
	private RedisConnection redis = new RedisConnection();
	private int floors;
	private int floor_capacity;  
	private VehicleFactory fac = new VehicleFactory();
	private SimpleDateFormat  sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
	private int total_capacity = 8;
	 public int alotLotForVehicle(Vehicle obj){  
		 alotSlot();
		 char requested_lot = obj.lot_name.charAt(0);
		 String lot;
		 Set<String> parked_vehicles;
//		 System.out.println("forloop capacity check"+this.floor_capacity);
		 for(int j=0;j<this.floor_capacity;j++) {
			 lot = "" + requested_lot  + (j+1);
			 parked_vehicles = redis.getParkedVehiclesForLot(lot);
//			 System.out.println(lot + " size " + parked_vehicles.size() );
			 if(parked_vehicles.size() > 0) {
				 int car_count=0,cycle_count=0,bike_count=0;
				 Map<String,String> vehicle;
				 for(String parked_vehicle_number : parked_vehicles){
					 vehicle = redis.getParkedVehicle(parked_vehicle_number);
					   switch(vehicle.get("type")) {
					   case "1":
						   car_count++;
						   break;
					   case "2":
						   bike_count++;
						   break;
					   case "3":
						   cycle_count++;
						   break;
					   default:
						   break;
					   }
				 }
				 if(canParkInSameSlot(car_count,bike_count,cycle_count,obj.vehicle_type,requested_lot)) {
					 obj.lot_name = lot;
					 return redis.parkVehicle(obj);
				 }
			 }else{
//				 System.out.println(lot + " is empty parking");
				 if(obj.vehicle_type == 1) {
					 redis.updateLotFull(requested_lot);
				 }
				 obj.lot_name = lot;
				 return redis.parkVehicle(obj);
			 }
		 }
		 return 3;
	 }
	  
	 Boolean canParkInSameSlot(int car_count,int bike_count,int cycle_count,int vehicle_type,char requested_lot){
		 
		 int space_for_car = total_capacity;
		 int space_for_bike = (total_capacity/4);
		 int space_for_cycle = (total_capacity/8);
		 int lot_space = (space_for_car * car_count) + (space_for_bike * bike_count) + (space_for_cycle * cycle_count);
		 System.out.println("car_count : "+car_count+" bike_count : " + bike_count+" cycle_count : "+cycle_count + " vehicle_type : "+vehicle_type+""+" lot_space "+lot_space );
		 switch(vehicle_type) {
		 case 1:
//			 System.out.println(lot_space + "while parking Car");
			 if(lot_space == 0) {
				 checkLotFullAndUpdate(total_capacity,requested_lot);
				 return true;
			 }
			 break;
		 case 2:
//			 System.out.println(lot_space + "while parking Bike");
			 if((lot_space + space_for_bike) <= total_capacity ) {
				 checkLotFullAndUpdate((lot_space + space_for_bike),requested_lot);
				 return true;
			 }			 
			 break;
		 case 3:
//			 System.out.println(lot_space + "while parking Cycle");
			 if((lot_space + space_for_cycle) <= total_capacity ) {
				 checkLotFullAndUpdate((lot_space + space_for_cycle),requested_lot);
				 return true;
			 }
			 break;
		 default:
			break;
		 }
		 return false;
	 }
	 
	 void checkLotFullAndUpdate(int capacity,char requested_lot){
//		 System.out.println("capacity full check for lot "+requested_lot+ " capacity : " + capacity);
		 if(capacity == total_capacity) {
			 System.out.println("updating lot "+requested_lot+ " full");
			 redis.updateLotFull(requested_lot);
		 }
	 }
	 public Vehicle findVehicle(int vehicle_number){  
		 alotSlot();
		 try {
			 Map<String,String> vehicle_attributes = redis.findVehicle(vehicle_number);
			 if(!vehicle_attributes.isEmpty()) {
				 int vehicle_type = Integer.parseInt(vehicle_attributes.get("type"));
				 Date entry_time = this.sdf.parse(vehicle_attributes.get("entry_time"));
				 String lot = vehicle_attributes.get("lot");
				 Vehicle vehicle = fac.getVehicle(vehicle_type,vehicle_number,entry_time,lot);
				 return vehicle;
			 }
			 return null;
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 return null;
		 }
	 }
	 public void alotSlot(){  
		 this.floors = redis.getFloors();
		 this.floor_capacity = redis.getFloorCapacity();
//		 System.out.println("Floors " + this.floors);
//		 System.out.println("Floor Capacity " + this.floor_capacity);
	 }  	 
	 
	 public String checkOutAndCalculate(Vehicle vehicle) {
		 alotSlot();
		 try {
		 Date entry = vehicle.entry_time;  
		 Date exit = vehicle.exit_time;
		 long timeDiff =exit.getTime() - entry.getTime(); 
		 long dayDiff = (timeDiff / (1000*60*60*24)) % 365;  
		 long hours_difference = (timeDiff / (1000*60*60)) % 24;   
		 long minutes_difference = (timeDiff / (1000*60)) % 60; 
		 hours_difference = hours_difference + (dayDiff * 24);
		 double price = calculatePriceForExit(vehicle.vehicle_type,hours_difference,minutes_difference) ;
		 String duration = hours_difference + " hours " + minutes_difference + " minutes";
		 checkLotFullAndDecreaseLotCount(vehicle.lot_name);
		 if(this.redis.checkOut(vehicle.vehicle_number,vehicle.lot_name)) {
			 this.redis.saveParking(vehicle.vehicle_number,entry,exit,vehicle.lot_name);
			 System.out.println("vehicle : "+ vehicle.vehicle_number+ "  checkin time "+ entry + " checkout time "+ exit + "  parsed time "+sdf.format(exit));
			 return "Total duration: "+duration+".Amount to be Paid:Rs: " + price; 
		 }
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
		 return "";
		 
	 }
	 
	 private double calculatePriceForExit(int vehicle_type,long hours_difference,long minutes_difference) {
		 double price;
		 switch(vehicle_type){
		 case 1:
			 price = ( hours_difference +  (((double)minutes_difference)/60)) * 50;
			 
			 return  price;
		 case 2:
			 price = ( hours_difference +  (((double)minutes_difference)/60)) * 10;
			 return price;
		 case 3:
			 price = ( hours_difference +  (((double)minutes_difference)/60)) * 6;
			 return price;
		default:
			return 0;
		 }
			
		}
	 
	 public List<Map<String,String>> parkingHistory(int vehicle_number) {
		 return this.redis.getParkingHistory(vehicle_number);
	 }
	 
	 public List<Map<String,Integer>> showLots() {
		 alotSlot();
		 return this.redis.getLots(floors);
	 }
	 
	 void checkLotFullAndDecreaseLotCount(String lot){
		 Set<String> parked_vehicles = redis.getParkedVehiclesForLot(lot);
			 int car_count=0,cycle_count=0,bike_count=0;
			 Map<String,String> vehicle;
			 for(String parked_vehicle_number : parked_vehicles){
				 vehicle = redis.getParkedVehicle(parked_vehicle_number);
				   switch(vehicle.get("type")) {
				   case "1":
					   car_count++;
					   break;
				   case "2":
					   bike_count++;
					   break;
				   case "3":
					   cycle_count++;
					   break;
				   default:
					   break;
				   }
			 }
			 int space_for_car = total_capacity;
			 int space_for_bike = (total_capacity/4);
			 int space_for_cycle = (total_capacity/8);
			 int lot_space = (space_for_car * car_count) + (space_for_bike * bike_count) + (space_for_cycle * cycle_count);
			 if(lot_space == total_capacity) {
				 redis.decreaseLotCount(lot.charAt(0) + "");
			 }
			 System.out.println("car_count : "+car_count+" bike_count : " + bike_count+" cycle_count : "+cycle_count +" lot space" + lot_space + "  lot  " +lot);
	 }
		 
	 }
  