import {useState} from 'react';
import './FindVehicle.css';
import {findVehicle as vehicleFind} from '../../services/LotsService';
export default function FindVehicle(){
    let [findVehicleState,setFindVehicleState] = useState({message:"",status:false});
    let [parkingHistory,setParkingHistory] = useState([]);
    let [forminputs,setforminputs] = useState({
        vehicle_number:0
    });
    const findVehicle = (e) =>{
        e.preventDefault();
        if(e.target.reportValidity()) {
            vehicleFind(forminputs)
            .then((result)=>{
                setFindVehicleState({message:result.message.vehiclefindresult,status:result.status});
                setParkingHistory(result.message.parkinghistory);
            })
            .catch((error)=>{
                setFindVehicleState({message:error.message,status:error.status});
            });
        }

    }
    let updateFormValues = (e) => {
        switch(e.target.id){
            case 'vnumber':
                setforminputs({...forminputs,vehicle_number:+(e.target.value)});
                break;
            default:
                break;
        }
    }
    const headers = ['FLOOR','CHECK IN','CHECK OUT'];
    return(
    <div className="form">
    <form onSubmit={findVehicle}>
        <div>
        <label htmlFor="vnumber">Vehicle Number *</label>
        <input type="number" id="vnumber" name="vnumber" required onChange={updateFormValues}/><br/>
        </div>
        <div className="div-btn">
        <input type="submit" className="btn" value="FIND VEHICLE"/>
        </div>
        {parkingHistory?.length > 0 ? <table>
            <thead>
                <tr>
                    {headers?.map((header,i) => <th key={i+'th'}>{header}</th>)}
                </tr>
            </thead>
            <tbody>
                {parkingHistory?.map((history,i) => <tr key={i+'td'}><td>{history.lot}</td><td>{history.entry_time}</td><td>{history.exit_time}</td></tr>)}
            </tbody>
        </table> : null }
        {findVehicleState.status ? <div className="find-msg">
            <h4>{findVehicleState.message}</h4>
        </div> : 
        <div className="find-error">
        <h4>{findVehicleState.message}</h4>
        </div>
    }
    </form>
    </div>
)}