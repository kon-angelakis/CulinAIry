import { useState } from "react"
import SubmitButton from "./Forms/SubmitButton"
import FormInput from "./Forms/FormInput"
import { FaRegUser } from "react-icons/fa"
import PasswordInput from "./Forms/PasswordInput"
import { MdPassword } from "react-icons/md"
import GoogleButton from "./Forms/GoogleButton"
import { Link } from "react-router-dom"

function LoginForm(){

    const [available, setAvailable] = useState(true)

    const [verified, setVerified] = useState(false)

    const [formData, setFormData] = useState({
        user: "",
        pass: ""
    })

    const handleChange = (e) =>{
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        })
    }

    const handleSubmit = (e) =>{
        e.preventDefault(); 
        setTimeout(() => {
        }, 1500);
  };

    return(
        <div className="container">
            <form onSubmit={handleSubmit} method="POST">
                <div className="form-container">
                    <div className="form-title">
                        <h1>Sign In</h1>
                    </div>
                    <hr/>
                    <div className="form-inputs">
                        <FormInput name="user" type="text" placeholder="Email or Username" required={true} available={available} icon={FaRegUser} animationIndex={1} onChange={handleChange}/>
                        <PasswordInput name="pass" placeholder="Password" icon={MdPassword} animationIndex={2} onChange={handleChange}/>
                        <SubmitButton
                            text={"Login"}
                            data={formData}
                            endpoint="http://192.168.1.70:1010/api/auth/login"
                            setVerified={setVerified}
                        />
                        <div className="helper-section">
                            <p>Don't have an account? <Link to="/register">Sign up</Link></p>
                            <p>Forgot your password? <a href="#">Request Change</a></p>
                        </div>
                    </div>
                    <GoogleButton/>
                </div>
                
            </form> 
        </div>

    )

}

export default LoginForm