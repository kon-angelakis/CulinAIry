import { useState } from "react";
import { LuEye, LuEyeClosed } from "react-icons/lu";

function PasswordInput({
  name,
  placeholder,
  icon: Icon,
  animationIndex,
  onChange,
}) {
  //React state to toggle password icon
  const [showPassword, setShowPassword] = useState(false);
  const togglePassword = () => {
    setShowPassword((prev) => !prev);
  };

  return (
    <div className="test">
      <div className="form-input" style={{ "--i": animationIndex }}>
        {Icon ? <Icon className="icon" /> : <span className="icon"></span>}
        <input
          name={name}
          placeholder=" "
          type={showPassword ? "text" : "password"}
          required
          onChange={onChange}
        />
        <label for={name} className="placeholder">
          {placeholder}
        </label>
        {showPassword ? (
          <LuEye className="icon" id="show-password" onClick={togglePassword} />
        ) : (
          <LuEyeClosed
            className="icon"
            id="show-password"
            onClick={togglePassword}
          />
        )}
      </div>
    </div>
  );
}

export default PasswordInput;
