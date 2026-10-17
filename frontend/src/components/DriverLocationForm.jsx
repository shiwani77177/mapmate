import { useState } from "react";

const STATUS_OPTIONS = ["OFFLINE", "AVAILABLE", "OFFERED", "ON_TRIP"];

export default function DriverLocationForm() {
  const [driverId, setDriverId] = useState("");
  const [lat, setLat] = useState("");
  const [lng, setLng] = useState("");
  const [status, setStatus] = useState("AVAILABLE");
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);
    setResult(null);
    setLoading(true);

    try {
      const res = await fetch(`/api/drivers/${driverId}/location`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          lat: parseFloat(lat),
          lng: parseFloat(lng),
          status,
        }),
      });

      const data = await res.json();

      if (!res.ok) {
        // Spring's default ProblemDetail body has "detail"; validation errors have "errors".
        const message =
          data.detail ||
          data.message ||
          JSON.stringify(data.errors) ||
          `Request failed (${res.status})`;
        throw new Error(message);
      }

      setResult(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div
      style={{
        marginTop: "2rem",
        padding: "1rem",
        border: "1px solid #ddd",
        borderRadius: 8,
        maxWidth: 360,
      }}
    >
      <h2>Update Driver Location</h2>
      <form onSubmit={handleSubmit} style={{ display: "grid", gap: "0.6rem" }}>
        <label>
          Driver ID
          <input
            value={driverId}
            onChange={(e) => setDriverId(e.target.value)}
            type="number"
            required
            style={{ display: "block", width: "100%" }}
          />
        </label>
        <label>
          Latitude
          <input
            value={lat}
            onChange={(e) => setLat(e.target.value)}
            type="number"
            step="any"
            required
            style={{ display: "block", width: "100%" }}
          />
        </label>
        <label>
          Longitude
          <input
            value={lng}
            onChange={(e) => setLng(e.target.value)}
            type="number"
            step="any"
            required
            style={{ display: "block", width: "100%" }}
          />
        </label>
        <label>
          Status
          <select
            value={status}
            onChange={(e) => setStatus(e.target.value)}
            style={{ display: "block", width: "100%" }}
          >
            {STATUS_OPTIONS.map((s) => (
              <option key={s} value={s}>
                {s}
              </option>
            ))}
          </select>
        </label>
        <button type="submit" disabled={loading}>
          {loading ? "Updating..." : "Update Location"}
        </button>
      </form>

      {error && <p style={{ color: "crimson" }}>Error: {error}</p>}
      {result && (
        <pre
          style={{
            background: "#f5f5f5",
            padding: "0.5rem",
            marginTop: "1rem",
            overflowX: "auto",
          }}
        >
          {JSON.stringify(result, null, 2)}
        </pre>
      )}
    </div>
  );
}
