import {useState} from 'react';
import './CheckOut.css';
import {checkOut as vehicleCheckOut} from '../../services/LotsService';
export default function CheckOut(){
    let [checkOutState,setCheckOutState] = useState({message:"",status:false});
    let [forminputs,setforminputs] = useState({
        vehicle_number:0,
        exit_time:1,
    });
    const checkOut = (e) =>{
        e.preventDefault();
        // console.log(forminputs.entry_time)
        let inputs  =  {...forminputs,exit_time:getDate(forminputs.exit_time)}
        console.log(inputs)
        if(e.target.reportValidity()) {
            vehicleCheckOut(inputs)
            .then((result)=>{
                setCheckOutState({message:result.message,status:result.status});
            })
            .catch((error)=>{
                setCheckOutState({message:error.message,status:error.status});
            });
        }

    }
    function getDate(date){
        const d = new Date(date);
        console.log(d)
        let h =  d.getHours();
        let min = d.getMinutes();
        let sec = d.getSeconds();
        var hours = h;
        var mid='AM';
        if(hours===0){ //At 00 hours we need to show 12 am
        hours=12;
        }
        else if(hours>12)
        {
        hours=hours%12;
        mid='PM';
        }else if(hours===12)
        {
        mid='PM';
        }
        return d.getDate() +"-"+  (d.getMonth()+1) +"-"+ d.getFullYear() +" "  + hours + ":" +min+":"+sec+" "+mid;
    }
    let updateFormValues = (e) => {
        switch(e.target.id){
            case 'vnumber':
                setforminputs({...forminputs,vehicle_number:+(e.target.value)});
                break;
            case 'checkouttime':
                setforminputs({...forminputs,exit_time:new Date(e.target.value)});
                break;
            default:
                break;
        }
    }
    return(
    <div className="form">
    <form onSubmit={checkOut}>
        <div>
        <label htmlFor="vnumber">Vehicle Number *</label>
        <input type="number" id="vnumber" name="vnumber" required onChange={updateFormValues}/><br/>
        </div>
        <div>
        <label htmlFor="checkouttime">CheckOut Time *</label>
         <input type="datetime-local" id="checkouttime" name="checkouttime" required onChange={updateFormValues}/>
         <br/><br/>
        </div>
        <div className="div-btn">
        <input type="submit" className="btn" value="CHECK OUT"/>
        </div>
        {checkOutState.status ? <div className="checkout-msg">
            <h4>{checkOutState.message}</h4>
        </div> : 
        <div className="checkout-error">
        <h4>{checkOutState.message}</h4>
        </div>
    }
    </form>
    </div>
)}