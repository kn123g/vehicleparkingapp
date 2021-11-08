package parking;

import java.util.Date;

public class Cycle extends Vehicle{
	Cycle(int vehicle_number,Date time,String slot){
		super(3);
		this.vehicle_number = vehicle_number;
		this.entry_time = time;
		this.lot_name = slot;
	}
	
}
