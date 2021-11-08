import { useHistory } from "react-router-dom";
import "./Home.css";

export default function Home() {
  const history = useHistory();
  const proceed = () => {
    history.push("/welcome");
  };
  return (
    <div className="home">
      <div className="home-head">
        <h3>Total no of floors : 4(A,B,C,D)</h3>
        <h3>Total capacity of all floor : 10</h3>
      </div>
      <table className="table">
        <thead>
          <tr>
            <th>TYPE OF VEHICLE</th>
            <th>COST/HOUR</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Car</td>
            <td>50/hr</td>
          </tr>
          <tr>
            <td>Bike</td>
            <td>10/hr</td>
          </tr>
          <tr>
            <td>Cycle</td>
            <td>6/hr</td>
          </tr>
        </tbody>
      </table>
      <div className="proceed">
        <button className="btn" onClick={proceed}>
          PROCEED
        </button>
      </div>
    </div>
  );
}
