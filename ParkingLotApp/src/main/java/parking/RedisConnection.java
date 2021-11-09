package parking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis; 
public class RedisConnection {

		private Jedis jedis = new Jedis("localhost"); 
		private SimpleDateFormat  sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
	   RedisConnection(){
//		   System.out.println("Redis is connected: "+ jedis.ping());
	   }
	   
	   int getFloors(){
		   return Integer.parseInt(jedis.get("floors")); 
	   }
	   int getFloorCapacity(){
		   return Integer.parseInt(jedis.get("floorcapacity")); 
	   }
	   
	   Set<String> getParkedVehiclesForLot(String requested_lot){
		 Set<String> lot_set = jedis.smembers("lot"+requested_lot);
		 return lot_set;
	   }
	   
	   Map<String,String> getParkedVehicle(String vehicle_number){
		   return jedis.hgetAll("parkedvehicle:"+vehicle_number);
	   }
	   
	   int parkVehicle(Vehicle vehicle) {
		   try {
			   String vehiclenumber = vehicle.vehicle_number.toString();
			   System.out.println("vehicle : "+ vehicle.vehicle_number+ "  checkin time "+vehicle.entry_time + "  parsed time "+sdf.format(vehicle.entry_time));
			   Map<String,String> vehicle_properties = Map.of(
					    "type", vehicle.vehicle_type.toString(),
					    "lot", vehicle.lot_name ,
					    "entry_time", sdf.format(vehicle.entry_time)
//					    "exit_time", vehicle.exit_time.toString()
				);
//			   System.out.println(vehiclenumber + " is parking in redis");
			   if(jedis.sadd("vehiclesparked", vehicle.vehicle_number.toString()) == 1) {
				   jedis.sadd("lot"+vehicle.lot_name, vehiclenumber);
				   jedis.hmset("parkedvehicle:"+vehiclenumber, vehicle_properties);
				   return 1;
			   }else {
				   return 0;
			   }
			   
		   }catch(Exception e) {
			   e.printStackTrace();
			   return 2;
		   }
		   		   
	   }
	   
	   Map<String,String> findVehicle(int vehicle_number){
		   try {
			   return jedis.hgetAll("parkedvehicle:"+vehicle_number);
		   }
		   catch(Exception e) {
			   return null;
		   }
	   }
	  
	   Boolean checkOut(int vehicle_number,String lot){
		   try {
			   jedis.srem("lot"+lot,vehicle_number+"");
			   jedis.hdel("parkedvehicle:"+vehicle_number,"type");
			   jedis.hdel("parkedvehicle:"+vehicle_number,"entry_time");
			   jedis.hdel("parkedvehicle:"+vehicle_number,"lot");
			   jedis.srem("vehiclesparked", vehicle_number + "");
			   return true;
		   }
		   catch(Exception e) {
			   return false;
		   }
	   }
	   void decreaseLotCount(String lot){
		   String lotCount = jedis.get("lotcount"+lot);
		   if(lotCount!=null) {
			   jedis.set("lotcount"+lot,(Integer.parseInt(lotCount)-1) + "");
			   System.out.println("decreased lotcount"+lot + "=" + (Integer.parseInt(lotCount)-1));
		   }
	   }
	   void saveParking(int vehicle_number,Date entry,Date exit,String lot) {
		   try {
			   long time = new Date().getTime();
			   Map<String,String> history_properties = Map.of(
					    "entry_time", sdf.format(entry),
					    "lot", lot ,
					    "exit_time", sdf.format(exit)
				);
			   jedis.lpush("vehiclehistory"+vehicle_number, vehicle_number+""+time);
			   jedis.hmset("history:"+vehicle_number+time, history_properties);
		   }
		   catch(Exception e) {
			   e.printStackTrace();
		   }
	   }
	   
	   List<Map<String,String>> getParkingHistory(int vehicle_number){
		   try {
			   List<Map<String,String>> history = new ArrayList<Map<String,String>>() ; 
			   List<String> histories = jedis.lrange("vehiclehistory"+vehicle_number, 0 , 5);
			   for(String history_id : histories) {
				   history.add(jedis.hgetAll("history:"+history_id));
			   }
			   return history;
		   }
		   catch(Exception e) {
			   e.printStackTrace();
			   return null;
		   }
		   
	   }
	   
	   void updateLotFull(char lot){
		   try {
			   String lotCount = jedis.get("lotcount"+lot);
			   if(lotCount!=null) {
				   jedis.set("lotcount"+lot,(Integer.parseInt(lotCount)+1) + "");
				   System.out.println("set lotcount"+lot + "=" + (Integer.parseInt(lotCount)+1));
			   }
			   else {
				  jedis.set("lotcount"+lot, "1");
			   }
		   }
		   catch(Exception e) {
			   e.printStackTrace();
		   }
	   }
	   
	   List<Map<String,Integer>> getLots(int lots){
		   try {
			   char lot_name =  (char)65 ;
			   List<Map<String,Integer>> history = new ArrayList<Map<String,Integer>>() ; 
			   Map<String,Integer> map ;
			   for(int i=0;i<lots;i++) {
				   char lot = (char)((lot_name) + i);
				   String lotcount = jedis.get("lotcount"+lot);
//				   System.out.println("lotcount"+lot + lotcount + "i=" +i);
				   if(lotcount != null) {
					   map = new HashMap<String,Integer> ();
					   map.put(lot+"", Integer.parseInt(lotcount));
					   history.add(map);
				   }
			   }
			   return history;
		   }
		   catch(Exception e) {
			   e.printStackTrace();
			   return null;
		   }
	   }
}
