function FormInput({ name, type, placeholder, available, required, maxlength, icon : Icon, animationIndex, onChange }){

    return(
        <div className="test">

            <div className="form-input" style={{ "--i": animationIndex }}>
                {Icon ? <Icon className={available ? "icon" : "icon unavailable"}/>
                : <span className="icon"></span>}
                <input className={available ? "input" : "input unavailable"} name={name} placeholder=" " type={type} maxLength={maxlength} onChange={onChange} required={required}></input>
                <label for={name} className="placeholder">{placeholder}</label>
            </div>
        </div>
    )
}

export default FormInput;