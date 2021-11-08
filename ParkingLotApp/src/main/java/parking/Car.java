package parking;

import java.util.Date;

public class Car extends Vehicle {

	Car(int vehicle_number,Date time,String slot){
		super(1);
		this.vehicle_number = vehicle_number;
		this.entry_time = time;
		this.lot_name = slot;
	}
	
	
}
