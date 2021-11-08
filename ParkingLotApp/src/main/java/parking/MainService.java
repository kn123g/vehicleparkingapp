package parking;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MainService {
	
	private VehicleFactory fac ;
	private Vehicle  v = null;
	private ServiceResponse  sr;
	public static MainService instance = new MainService();
	private Lots lots = new Lots();
	
	private MainService() {
		fac = new VehicleFactory();
//		lots.alotSlot();
	}
	
	public ServiceResponse checkIn(int type,int number,Date time,String lot) {
		v = fac.getVehicle(type,number,time,lot);
		sr = new ServiceResponse();
		switch(v.checkIn()) {
		case 0:
			sr.action = true;
			sr.message = "vehicle already parked";
			break;
		case 1:
			sr.action = true;
			sr.message = "Success: Vehicle parked at "+ v.lot_name;
			break;
		case 2:
			sr.action = false;
			sr.message = "unable to park technical issue";
			break;
		case 3:
			sr.action = false;
			sr.message = "Failure: Lot '"+ lot  + "' is full";
			break;
		default:
			break;
		
		}
		return sr;
	}
	
	public ServiceResponse checkOut(int vehicle_number,Date checkout_time) {
		v = lots.findVehicle(vehicle_number);
		sr = new ServiceResponse();
		if(v!=null) {
			v.exit_time = checkout_time;
			sr.message = "Success: Vehicle(" + VehicleType.Vehicles.values()[v.vehicle_type] + ','+ v.vehicle_number + ") is available in " + v.lot_name + ".";
			String checkout_message = v.checkOut();
			sr.message = sr.message + checkout_message;
			if(checkout_message !="") {
				sr.action = true;
			}
		}else {
			sr.action = false;
			sr.message = "Faliure: Vehicle("+ vehicle_number +") is not available";
		}
		return sr;
	}
	public ServiceResponse findMyVehicle(int vehicle_number) {
		v = lots.findVehicle(vehicle_number);
		sr = new ServiceResponse();
		Gson gson = new Gson();
		String vehicleFindResult ;
		if(v != null) {
			sr.action = true;
			vehicleFindResult = v.findMyVehicle();
		}
		else {
			sr.action = false;
			vehicleFindResult = "Faliure: Vehicle("+ vehicle_number +") is not available";
		}

	    JsonArray parkingHistoryJson = new Gson().toJsonTree(lots.parkingHistory(vehicle_number)).getAsJsonArray();
	    JsonParser parser = new JsonParser();

	    JsonElement jsonElement = parser.parse(gson.toJson(parkingHistoryJson));
	    JsonObject jsonHistoryObject = new JsonObject();
	    jsonHistoryObject.add("parkinghistory", jsonElement);
	    jsonHistoryObject.addProperty("vehiclefindresult", vehicleFindResult);	
	    sr.message = jsonHistoryObject.toString();
		return sr;
	}

	public ServiceResponse showLots() {
		try {
			sr = new ServiceResponse();
			Gson gson = new Gson();
			sr.message =gson.toJson(lots.showLots());
			sr.action = true;
			return sr;
			
		}catch(Exception e) {
			e.printStackTrace();
			sr.action = false;
			return sr;
			
		}
	}
}

//Sample case 1
//1,1234,Car,31-10-2021 5:00 AM,A
//1,1235,Car,31-10-2021 5:02 AM,A
//1,1335,Car,31-10-2021 5:05 AM,A
//1,7865,Car,31-10-2021 5:06 AM,A
//1,3659,Car,31-10-2021 5:07 AM,A
//1,1305,Car,31-10-2021 5:08 AM,A
//2,1234,31-10-2021 7:30 AM,A
//3,1235


//Sample case2
//1,1234,Car,31-10-2021 5:00 AM,A
//2,1235,31-10-2021 7:10 AM
//3,1235

//Sample test case3
//1,1234,Car,31-10-2021 5:00 AM,A
//2,1234,01-11-2021 7:10 AM


//Sample test case for cycle 1
//1,1234,Car,31-10-2021 5:00 AM,A
//1,1235,Car,31-10-2021 5:02 AM,A
//1,1335,Cycle,31-10-2021 5:05 AM,B

//Sample test case for cycle 2
//1,1335,Cycle,31-10-2021 5:00 AM,B
//2,1335,31-10-2021 7:00 AM


//history test case 1
//1,1234,Car,31-10-2021 5:00 AM,A
//2,1234,31-10-2021 7:10 AM
//1,1234,Car,31-10-2021 8:00 AM,B
//2,1234,31-10-2021 9:00 AM
//3,1234

//history test case 2
//3,1234

//history testcase 3
//1,1234,Car,31-10-2021 5:00 AM,A
//2,1234,31-10-2021 7:10 AM
//1,1234,Car,31-10-2021 8:00 AM,B
//2,1234,31-10-2021 9:00 AM
//1,1234,Car,31-10-2021 9:15 AM,B
//3,1234

