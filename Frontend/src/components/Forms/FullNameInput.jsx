function FullNameInput({ name1, name2, required, icon : Icon, animationIndex, onChange }){
    return(
        <div className="form-input" style={{ "--i": animationIndex }}>
            {Icon ? <Icon className="icon" /> : <span className="icon"></span>}
            <div className="form-firstname">
                <input name={name1} type="text" placeholder="First Name" required={required} onChange={onChange}></input>
            </div>
            <div className="form-lastname">
                <input name={name2} type="text" placeholder="Last Name" required={required} onChange={onChange}></input>
            </div>
        </div>
    )
}

export default FullNameInput;