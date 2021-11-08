import { useState } from 'react';
import './CheckIn.css';
import {checkIn as vehicleCheckIn} from '../../services/LotsService';
export default function CheckIn(){
    let [parkState,setParkState] = useState({message:"",status:false});
    let [forminputs,setforminputs] = useState({
        vehicle_number:0,
        vehicle_type:1,
        lot_name:'A',
        entry_time:new Date(),
    });
    const checkIn = (e) =>{
        e.preventDefault();
        // console.log(forminputs.entry_time)
        let inputs  =  {...forminputs,entry_time:getDate(forminputs.entry_time)}
        console.log(inputs)
        if(e.target.reportValidity()) {
            vehicleCheckIn(inputs)
            .then((result)=>{
                setParkState({message:result.message,status:result.status});
            })
            .catch((error)=>{
                setParkState({message:error.message,status:error.status});
            });
            // setParkState("*The Vehicle is parked at A1");
            // console.log(forminputs);
        }

    }
    function getDate(date){
        const d = new Date(date);
        console.log(d)
        let h =  d.getHours();
        let m = d.getMinutes();
        let a = (h > 12) ? (h-12 + ':' + m+ ':'+d.getSeconds() +' PM') : (((h===0)?'12:' + m +':'+d.getSeconds()+' AM':h +':' + m +':'+d.getSeconds()+' PM') );
        return d.getDate() +"-"+  (d.getMonth()+1) +"-"+ d.getFullYear() +" "  + a;
    }
    let updateFormValues = (e) => {
        switch(e.target.id){
            case 'vnumber':
                setforminputs({...forminputs,vehicle_number:+(e.target.value)});
                break;
            case 'vtypecar':
                setforminputs({...forminputs,vehicle_type:+e.target.value});
                break;
            case 'vtypebike':
                setforminputs({...forminputs,vehicle_type:+e.target.value});
                break;
            case 'vtypecycle':
                setforminputs({...forminputs,vehicle_type:+e.target.value});
            break;
            case 'lot':
                setforminputs({...forminputs,lot_name:e.target.value});
                break;
            case 'checkintime':
                setforminputs({...forminputs,entry_time:new Date(e.target.value)});
                break;
            default:
                break;
            
        }
    }
    return(
    <div className="form">
    <form onSubmit={checkIn}>
        <div>
        <label htmlFor="vnumber">Vehicle Number *</label>
        <input type="number" id="vnumber" name="vnumber" required onChange={updateFormValues}/><br/>
        </div>
        <div>
        <label htmlFor="vtype">Vehicle Type *</label>
         <input type="radio" id="vtypecar" name="vtype" value="1" required onChange={updateFormValues}/>Car
         <input type="radio" id="vtypebike" name="vtype" value="2" required onChange={updateFormValues}/>Bike
         <input type="radio" id="vtypecycle" name="vtype" value="3" required onChange={updateFormValues}/>Cycle
         <br/><br/>
        </div>
         <div>
        <label htmlFor="lot">Enter Lot *</label>
        <select id="lot" name="lot" onChange={updateFormValues} className="lot-input">
            <option value="A">A</option>
            <option value="B">B</option>
            <option value="C">C</option>
            <option value="D">D</option>
        </select><br/>
        </div>
        <div>
        <label htmlFor="checkintime">Check in Time *</label>
        <input type="datetime-local" id="checkintime" name="checkintime" required onChange={updateFormValues}/><br/>
        </div>
        <div className="div-btn">
        <input type="submit" className="btn" value="CHECK IN"/>
        </div>
        {parkState.status ? <div className="park-msg">
            <h4>{parkState.message}</h4>
        </div> : 
        <div className="park-error">
        <h4>{parkState.message}</h4>
        </div>
    }
        
    </form>
    </div>
    );
}