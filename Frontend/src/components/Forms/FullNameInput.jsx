function FullNameInput({ required, icon : Icon, animationIndex }){
    return(
        <div className="form-input" style={{ "--i": animationIndex }}>
            {Icon ? <Icon className="icon" /> : <span className="icon"></span>}
            <div className="form-firstname">
                <input type="text" placeholder="First Name" required={required}></input>
            </div>
            <div className="form-lastname">
                <input type="text" placeholder="First Name" required={required}></input>
            </div>
        </div>
    )
}

export default FullNameInput;