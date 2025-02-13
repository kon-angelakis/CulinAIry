import { FaRegAddressCard } from "react-icons/fa";
import { FaRegEnvelope } from "react-icons/fa6";
import { FaRegUser } from "react-icons/fa";
import { MdPassword } from "react-icons/md";
import { GiEnvelope } from "react-icons/gi";
import { RiVerifiedBadgeFill } from "react-icons/ri";


import FormInput from "./Forms/FormInput";
import FullNameInput from "./Forms/FullNameInput";
import PasswordInput from "./Forms/PasswordInput";
import OTPInput from "./Forms/OTPInput";

import axios from "axios";

import "./Forms.css";
import { useState } from "react";
import SubmitButton from "./Forms/SubmitButton";
import { Link } from "react-router-dom";

function RegisterForm() { 

    const [submitted, setSubmitStatus] = useState(false)
    const [verified, setVerified] = useState(false)
    const [fieldsAvailable, setFieldsAvailable] = useState({
        username: true,
        email: true
    })

    const handleSubmit = (e) =>{
        e.preventDefault(); 
        setTimeout(() => {
            setSubmitStatus(true); 
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
        user: ""
    });

    const handleFormChange = (e) =>{
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleFormValidation = (e) => {
 
        const updatedFormData = { ...formData, ["user"]: e.target.value };
        //Basically does an api request to check if the user/email are already in use
        setTimeout(()=>{
            axios.post("http://192.168.1.70:1010/api/auth/register/validate", updatedFormData)
            .then(response => {
                console.log('Post Created:', response.data);
                setFieldsAvailable({...fieldsAvailable, [e.target.name] : false})
            })
            .catch(error => {
                console.error('Error creating post:', error);
                setFieldsAvailable({...fieldsAvailable, [e.target.name] : true})
            });
        }, 250)
    };
    

    const passwordsMatch = formData.password == formData.cpassword;

    return (
        <div className="container">
            {!submitted ? (
                <form onSubmit={handleSubmit} method="POST">
                    <div className="form-container">
                        <div className="form-title">
                            <h1>Sign Up</h1>
                        </div>
                        <hr/>
                        <div className="form-inputs">
                            <FullNameInput name1="firstname" placeholder1="First Name" name2="lastname" placeholder2="Last Name" value={formData.fullname} onChange={handleFormChange} required={false} icon={FaRegAddressCard} animationIndex={1} />                            
                            <FormInput name="email" value={formData.email} onChange={(e)=>{handleFormChange(e); handleFormValidation(e)}} type="email" placeholder="Email" available={fieldsAvailable.email} required={true} icon={FaRegEnvelope} animationIndex={2}/>
                            <FormInput name="username" value={formData.username} onChange={(e)=>{handleFormChange(e); handleFormValidation(e)}} type="text" placeholder="Username" available={fieldsAvailable.username} required={true} icon={FaRegUser} animationIndex={3}/>
                            <PasswordInput name="password" value={formData.password} onChange={handleFormChange} placeholder="Password" icon={MdPassword} animationIndex={4} />
                            <PasswordInput name="cpassword" value={formData.cpassword} onChange={handleFormChange} placeholder="Confirm Password" animationIndex={5} />
                            
                            <SubmitButton
                                text={"Register"}
                                data={formData}
                                endpoint="http://192.168.1.70:1010/api/auth/register"
                                disabled={!passwordsMatch || !fieldsAvailable.email || !fieldsAvailable.username }
                            />
                        </div>
                        <div className="helper-section">
                            <p>Already have an account? <Link to="/login">Sign in</Link></p>
                        </div>
                    </div>
                </form> 
            ):
            (
            <>
                {!verified ? (
                    <form className="otp-container" onSubmit={handleSubmit} method="POST">
                        <GiEnvelope className="otp-icon" />                        
                        <h3>An activation email has been sent to <strong>{formData.email}</strong>. Please enter the OTP code to continue with the registration. If you can't find the email do check your <strong>spam folder</strong> or wait for a few moments.</h3>
                        <OTPInput otp={formData.otp} setOtp={(value) => setFormData({ ...formData, otp: value })} />

                        <SubmitButton
                            text={"Verify"}
                            data={formData}
                            endpoint="http://192.168.1.70:1010/api/auth/verify"
                            setVerified= {setVerified}
                        />
                        <SubmitButton
                            text={"Resend Verification Email"}
                            data={formData}
                            endpoint="http://192.168.1.70:1010/api/auth/register"
                        />
                    </form>
                ) :
                    <div className="otp-container">
                        <RiVerifiedBadgeFill className="otp-success-icon" />                        
                        <h3>Activation successful! You can now login to your account</h3>
                    </div>
                }
            </>
            )}
     
        </div>
    );
}

export default RegisterForm;


