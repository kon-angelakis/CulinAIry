import { useState } from "react";
import { FaRegAddressCard } from "react-icons/fa";
import { FaRegEnvelope } from "react-icons/fa6";
import { FaRegUser } from "react-icons/fa";
import { MdPassword } from "react-icons/md";

import FormInput from "./Forms/FormInput";
import FullNameInput from "./Forms/FullNameInput";
import PasswordInput from "./Forms/PasswordInput";

import "./Forms.css";

function RegisterForm() {

    return (
        <div className="container">
            <form>
                <div className="form-container">
                    <div className="form-title">
                        <h1>Sign Up</h1>
                    </div>
                    <div className="form-inputs">
                        <FullNameInput required={true} icon={FaRegAddressCard} animationIndex={1} />
                        <FormInput type="email" placeholder="Email" required={true} icon={FaRegEnvelope} animationIndex={2}/>
                        <FormInput type="text" placeholder="Username" required={true} icon={FaRegUser} animationIndex={3}/>
                        <PasswordInput placeholder="Password" icon={MdPassword} animationIndex={4} />
                        <PasswordInput placeholder="Confirm Password" animationIndex={5} />
                    </div>
                </div>
            </form>
        </div>
    );
}

export default RegisterForm;
