import { useState, useEffect } from "react";
import { LuUser, LuLock } from "react-icons/lu";
import SubmitButton from "./Elements/SubmitButton";
import FormInput from "./Elements/FormInput";
import PasswordInput from "./Elements/PasswordInput";
import GoogleButton from "./Elements/GoogleButton";
import { Link, useNavigate } from "react-router-dom";
import Splitter from "./Elements/Splitter";

function LoginForm() {
  const navigate = useNavigate();

  const [verified, setVerified] = useState(null);

  const [formData, setFormData] = useState({
    user: "",
    pass: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  useEffect(() => {
    if (verified === true) {
      setTimeout(() => {
        navigate("/home");
      }, 2500);
    } else if (verified === false) {
      alert("Invalid username or password");
    }
  }, [verified, navigate]);

  const handleSubmit = (e) => {
    e.preventDefault();
    setVerified(null); //resets the verified status to null so the useEffect works nicely
  };

  return (
    <div className="container">
      <form onSubmit={handleSubmit} method="POST">
        <div className="form-container">
          <div className="form-title">
            <h1>Sign In</h1>
          </div>
          <hr />
          <div className="form-inputs">
            <FormInput
              name="user"
              type="text"
              placeholder="Email or Username"
              required={true}
              available={true}
              icon={LuUser}
              animationIndex={1}
              onChange={handleChange}
            />
            <PasswordInput
              name="pass"
              placeholder="Password"
              icon={LuLock}
              animationIndex={2}
              onChange={handleChange}
            />
            <SubmitButton
              text={"Login"}
              data={formData}
              endpoint="/api/auth/login"
              method="post"
              setVerified={setVerified}
              formAction="Login"
              isAuthenticatedRequest={false}
              width={"100%"}
            />
            <div className="helper-section">
              <p>
                Don't have an account? <Link to="/register">Sign up</Link>
              </p>
              <p>
                Forgot your password? <a href="#">Request Change</a>
              </p>
            </div>
          </div>
          <Splitter text="Or sign in with" />
          <div className="alt-sign-in-options">
            <GoogleButton width={"100%"} />
          </div>
        </div>
      </form>
    </div>
  );
}

export default LoginForm;
