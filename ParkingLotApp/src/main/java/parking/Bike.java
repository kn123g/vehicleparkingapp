package parking;

import java.util.Date;

public class Bike extends Vehicle{
	Bike(int vehicle_number,Date time,String slot){
		super(2);
		this.vehicle_number = vehicle_number;
		this.entry_time = time;
		this.lot_name = slot;
	}
}
