import { useEffect, useState } from "react";

function App() {
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch("/api/ping")
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json();
      })
      .then(setData)
      .catch((e) => setError(e.message));
  }, []);

  return (
    <div style={{ padding: 24, fontFamily: "sans-serif" }}>
      <h1>MapMate</h1>
      {error && <p style={{ color: "red" }}>Backend error: {error}</p>}
      {!data && !error && <p>Connecting to backend...</p>}
      {data && (
        <p>
          Backend: <b>{data.status}</b> | PostGIS: <b>{data.postgis}</b>
        </p>
      )}
    </div>
  );
}

export default App;
