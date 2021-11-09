import axios from 'axios';
let api = `http://localhost:8080/ParkingLotApp/`;

export function checkIn(checkInInput){
    return axios.post(api+'checkin',checkInInput)
    .then((result) =>{
        return {message:result.data,status:true};
    })
    .catch((error)=>{
        return {message:error.response.data,status:false};
    });
}

export function checkOut(checkOutInput){
    return axios.post(api+'checkout',checkOutInput)
    .then((result) =>{
        return {message:result.data,status:true};
    })
    .catch((error)=>{
        return {message:error.response.data,status:false};
    });
}

export function findVehicle(FindInput){
    return axios.get(api+'findvehicle/'+FindInput.vehicle_number)
    .then((result) =>{
        return {message:result.data,status:true};
    })
    .catch((error)=>{
        return {message:error.response.data,status:false};
    });
}


export function showLots(vehicle_number){
    return axios.get(api+'showlots')
    .then((result) =>{
        return {message:result.data,status:true};
    })
    .catch((error)=>{
        return {message:error.response.data,status:false};
    });
}