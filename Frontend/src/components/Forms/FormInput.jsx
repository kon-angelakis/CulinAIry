function FormInput({ name, type, placeholder, required, maxlength, icon : Icon, animationIndex, onChange }){

    return(
        <div className="form-input" style={{ "--i": animationIndex }}>
            {Icon ? <Icon className="icon" /> : <span className="icon"></span>}
            <input name={name} type={type} maxLength={maxlength} placeholder={placeholder} onChange={onChange} required={required}></input>
        </div>
    )
}

export default FormInput;