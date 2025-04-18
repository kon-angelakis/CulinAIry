import { LuContact, LuUser, LuLock, LuMail } from "react-icons/lu";
import { GiEnvelope } from "react-icons/gi";
import { RiVerifiedBadgeFill } from "react-icons/ri";

import FormInput from "./Elements/FormInput";
import FullNameInput from "./Elements/FullNameInput";
import PasswordInput from "./Elements/PasswordInput";
import OTPInput from "./Elements/OTPInput";

import "./Forms.css";
import { useState } from "react";
import SubmitButton from "./Elements/SubmitButton";
import { Link } from "react-router-dom";
import authAxios from "../../config/axiosConfig";

function RegisterForm() {
  const [registered, setRegistered] = useState(false);
  const [completedRegistration, setCompletedRegistration] = useState(false);
  const [fieldsAvailable, setFieldsAvailable] = useState({
    username: true,
    email: true,
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    setTimeout(() => {
      setRegistered(true);
    }, 1500);
  };

  const [formData, setFormData] = useState({
    firstname: "",
    lastname: "",
    email: "",
    username: "",
    password: "",
    cpassword: "",
    otp: "",
    user: "",
  });

  const handleFormChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleFormValidation = (e) => {
    const updatedFormData = { ...formData, ["user"]: e.target.value };
    //Basically does an api request to check if the user/email are already in use
    setTimeout(() => {
      authAxios
        .post("/api/auth/register/validate", updatedFormData)
        .then((response) => {
          console.log("Post Created:", response.data);
          setFieldsAvailable({ ...fieldsAvailable, [e.target.name]: false });
        })
        .catch((error) => {
          console.error("Error creating post:", error);
          setFieldsAvailable({ ...fieldsAvailable, [e.target.name]: true });
        });
    }, 250);
  };

  const passwordsMatch = formData.password == formData.cpassword;

  return (
    <div className="container">
      {!registered ? ( //Initial state user awaits for the OTP
        <form onSubmit={handleSubmit} method="POST">
          <div className="form-container">
            <div className="form-title">
              <h1>Sign Up</h1>
            </div>
            <hr />
            <div className="form-inputs">
              <FullNameInput
                name1="firstname"
                placeholder1="First Name"
                name2="lastname"
                placeholder2="Last Name"
                value={formData.fullname}
                onChange={handleFormChange}
                required={false}
                icon={LuContact}
                animationIndex={1}
              />
              <FormInput
                name="email"
                value={formData.email}
                onChange={(e) => {
                  handleFormChange(e);
                  handleFormValidation(e);
                }}
                type="email"
                placeholder="Email"
                available={fieldsAvailable.email}
                warning="Already in use"
                required={true}
                icon={LuMail}
                animationIndex={2}
              />
              <FormInput
                name="username"
                value={formData.username}
                onChange={(e) => {
                  handleFormChange(e);
                  handleFormValidation(e);
                }}
                type="text"
                placeholder="Username"
                available={fieldsAvailable.username}
                warning="Already in use"
                required={true}
                icon={LuUser}
                animationIndex={3}
              />
              <PasswordInput
                name="password"
                value={formData.password}
                onChange={handleFormChange}
                placeholder="Password"
                icon={LuLock}
                animationIndex={4}
              />
              <PasswordInput
                name="cpassword"
                value={formData.cpassword}
                onChange={handleFormChange}
                placeholder="Confirm Password"
                animationIndex={5}
              />

              <SubmitButton
                text={"Register"}
                data={formData}
                endpoint="/api/auth/register"
                method="post"
                disabled={
                  !passwordsMatch ||
                  !fieldsAvailable.email ||
                  !fieldsAvailable.username
                }
                formAction="Send OTP"
                isAuthenticatedRequest={false}
                width={"100%"}
              />
            </div>
            <div className="helper-section">
              <p>
                Already have an account? <Link to="/login">Sign in</Link>
              </p>
            </div>
          </div>
        </form>
      ) : (
        <>
          {!completedRegistration ? ( //Show the OTP field
            <form
              className="otp-container"
              onSubmit={handleSubmit}
              method="POST"
            >
              <GiEnvelope className="otp-icon" />
              <h3>
                An activation email has been sent to{" "}
                <strong>{formData.email}</strong>. Please enter the OTP code to
                continue with the registration. If you can't find the email do
                check your <strong>spam folder</strong> or wait for a few
                moments.
              </h3>
              <OTPInput
                otp={formData.otp}
                setOtp={(value) => setFormData({ ...formData, otp: value })}
              />

              <SubmitButton
                text={"Verify"}
                data={formData}
                endpoint="/api/auth/verify"
                method="post"
                setVerified={setCompletedRegistration}
                formAction="Verify OTP and Register"
                isAuthenticatedRequest={false}
              />
              <SubmitButton
                primary={false}
                text={"Resend Verification Email"}
                data={formData}
                endpoint="/api/auth/register"
                method="post"
                formAction="Resend OTP"
                isAuthenticatedRequest={false}
              />
            </form>
          ) : (
            //Final state: completed registration and can redirect to login page
            <div className="otp-container">
              <RiVerifiedBadgeFill className="otp-success-icon" />
              <h3>
                Activation successful! You can now{" "}
                <Link to="/login">Login </Link> to your account
              </h3>
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default RegisterForm;
