function FormInput({ type, placeholder, required, icon : Icon, animationIndex}){

    return(
        <div className="form-input" style={{ "--i": animationIndex }}>
            {Icon ? <Icon className="icon" /> : <span className="icon"></span>}
            <input type={type} placeholder={placeholder} required={required}></input>
        </div>
    )
}

export default FormInput;