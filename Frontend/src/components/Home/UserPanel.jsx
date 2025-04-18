import "./UserPanel.css";

export default function UserPanel({ height, name, pfp }) {
  return (
    <div className="panel-container" style={{ height: height }}>
      <div className="app-section">
        <div className="app-logo">
          <img src="https://t4.ftcdn.net/jpg/06/71/92/37/360_F_671923740_x0zOL3OIuUAnSF6sr7PuznCI5bQFKhI0.jpg"></img>
        </div>
        <div className="app-name">CulinAIry</div>
      </div>
      <div className="user-section">
        <div className="user-message">
          <div className="welcoming">Welcome back,</div>
          <div className="name">{name}</div>
        </div>
        <div className="user-pfp">
          <img src={pfp}></img>
        </div>
      </div>
    </div>
  );
}
