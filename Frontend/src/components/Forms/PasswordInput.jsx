import { useState } from "react";
import { LuEye, LuEyeClosed } from "react-icons/lu";

function PasswordInput({ name, placeholder, icon : Icon, animationIndex, onChange }) {
    //React state to toggle password icon
    const [showPassword, setShowPassword] = useState(false);
    const togglePassword = () => {
        setShowPassword((prev) => !prev);
    };

    return (
        <div className="form-input" style={{ "--i" : animationIndex}}>
            {Icon ? <Icon className="icon" /> : <span className="icon"></span>}
            <input
                name = {name}
                type={showPassword ? "text" : "password"}
                placeholder={placeholder}
                required
                onChange={onChange}
            />
            {showPassword ? (
                <LuEye className="icon" id="show-password" onClick={togglePassword} />
            ) : (
                <LuEyeClosed className="icon" id="show-password" onClick={togglePassword} />
            )}
        </div>
    );
}

export default PasswordInput;
