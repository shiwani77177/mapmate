import { useState } from "react";

export default function NearbyDrivers() {
  const [lat, setLat] = useState("19.0760");
  const [lng, setLng] = useState("72.8777");
  const [radiusKm, setRadiusKm] = useState("5");
  const [results, setResults] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSearch = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setResults(null);

    try {
      const res = await fetch(
        `/api/drivers/nearby?lat=${lat}&lng=${lng}&radiusKm=${radiusKm}`,
      );
      const data = await res.json();
      if (!res.ok) {
        setError(data.message || JSON.stringify(data));
      } else {
        setResults(data);
      }
    } catch (err) {
      setError("Network error: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        marginTop: "2rem",
        padding: "1rem",
        border: "1px solid #ccc",
        borderRadius: "8px",
      }}
    >
      <h2 style={{ marginTop: 0 }}>🗺️ Nearby Drivers</h2>
      <form
        onSubmit={handleSearch}
        style={{
          display: "flex",
          gap: "0.5rem",
          flexWrap: "wrap",
          alignItems: "flex-end",
        }}
      >
        <label
          style={{
            display: "flex",
            flexDirection: "column",
            fontSize: "0.85rem",
          }}
        >
          Latitude
          <input
            type="number"
            step="any"
            value={lat}
            onChange={(e) => setLat(e.target.value)}
            style={{ padding: "0.4rem", width: "120px" }}
          />
        </label>
        <label
          style={{
            display: "flex",
            flexDirection: "column",
            fontSize: "0.85rem",
          }}
        >
          Longitude
          <input
            type="number"
            step="any"
            value={lng}
            onChange={(e) => setLng(e.target.value)}
            style={{ padding: "0.4rem", width: "120px" }}
          />
        </label>
        <label
          style={{
            display: "flex",
            flexDirection: "column",
            fontSize: "0.85rem",
          }}
        >
          Radius (km)
          <input
            type="number"
            step="0.1"
            min="0.1"
            max="50"
            value={radiusKm}
            onChange={(e) => setRadiusKm(e.target.value)}
            style={{ padding: "0.4rem", width: "80px" }}
          />
        </label>
        <button
          type="submit"
          disabled={loading}
          style={{ padding: "0.4rem 1rem", cursor: "pointer" }}
        >
          {loading ? "Searching..." : "Search"}
        </button>
      </form>

      {error && (
        <p style={{ color: "red", marginTop: "1rem" }}>Error: {error}</p>
      )}

      {results !== null && (
        <div style={{ marginTop: "1rem" }}>
          <p style={{ fontWeight: "bold" }}>Found {results.length} driver(s)</p>
          {results.length > 0 && (
            <table
              style={{
                borderCollapse: "collapse",
                width: "100%",
                fontSize: "0.85rem",
              }}
            >
              <thead>
                <tr style={{ background: "#f0f0f0" }}>
                  <th style={th}>Driver ID</th>
                  <th style={th}>Lat</th>
                  <th style={th}>Lng</th>
                  <th style={th}>Status</th>
                  <th style={th}>Distance</th>
                </tr>
              </thead>
              <tbody>
                {results.map((d) => (
                  <tr key={d.driverId}>
                    <td style={td}>{d.driverId}</td>
                    <td style={td}>{d.lat.toFixed(5)}</td>
                    <td style={td}>{d.lng.toFixed(5)}</td>
                    <td style={td}>{d.status}</td>
                    <td style={td}>
                      {(d.distanceMeters / 1000).toFixed(2)} km
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
}

const th = {
  padding: "0.4rem 0.6rem",
  border: "1px solid #ccc",
  textAlign: "left",
};
const td = { padding: "0.4rem 0.6rem", border: "1px solid #eee" };
