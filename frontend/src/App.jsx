import { useEffect, useState } from "react";
import DriverLocationForm from "./components/DriverLocationForm";

function App() {
  const [status, setStatus] = useState("checking...");
  const [postgis, setPostgis] = useState(null);

  useEffect(() => {
    fetch("/api/ping")
      .then((res) => res.json())
      .then((data) => {
        setStatus(data.status);
        setPostgis(data.postgis);
      })
      .catch(() => setStatus("DOWN"));
  }, []);

  return (
    <div style={{ padding: "2rem", fontFamily: "sans-serif" }}>
      <h1>MapMate</h1>
      <p>
        Backend status: <strong>{status}</strong>
      </p>
      {postgis && <p>PostGIS: {postgis}</p>}

      <DriverLocationForm />
    </div>
  );
}

export default App;
