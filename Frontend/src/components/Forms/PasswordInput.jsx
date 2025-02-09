import { useState } from "react";
import { GiHidden } from "react-icons/gi";
import { LuEye, LuEyeClosed } from "react-icons/lu";

function PasswordInput({ placeholder, icon : Icon, animationIndex }) {
    //React state to toggle password icon
    const [showPassword, setShowPassword] = useState(false);
    const togglePassword = () => {
        setShowPassword((prev) => !prev);
    };

    return (
        <div className="form-input" style={{ "--i" : animationIndex}}>
            {Icon ? <Icon className="icon" /> : <span className="icon"></span>}
            <input
                type={showPassword ? "text" : "password"}
                placeholder={placeholder}
                required
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
