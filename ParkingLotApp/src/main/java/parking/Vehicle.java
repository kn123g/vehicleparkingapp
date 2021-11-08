package parking;

import java.util.Date; 

public class Vehicle {
	Integer vehicle_number;
	Integer vehicle_type;
	Date entry_time;
	Date exit_time;
	String lot_name;	
	static Lots lots = new Lots();
	
	Vehicle(Integer type){
		this.vehicle_type = type;
	}
	int checkIn() {
		int value = lots.alotLotForVehicle(this);
//		System.out.println("checkIn result " + value);
		return value;
	}
	String checkOut() {
		return lots.checkOutAndCalculate(this);
	}
	String findMyVehicle() {
		return "Vehicle(" + VehicleType.Vehicles.values()[this.vehicle_type] + ','+ this.vehicle_number + ") is parked at the lot " + this.lot_name + " on " + this.entry_time +  ".";
	}
	
}
