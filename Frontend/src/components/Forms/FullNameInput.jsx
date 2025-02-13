function FullNameInput({ name1, placeholder1, name2, placeholder2, required, icon : Icon, animationIndex, onChange }){
    return(
        <div className="test">
            <div className="form-input">
                {Icon ? <Icon className="icon" /> : <span className="icon"></span>}
                <input name={name1} placeholder=" " type="text" required={required} onChange={onChange}></input>
                <label for={name1} className="placeholder">{placeholder1}</label>

            </div>
            <div className="form-input">
                {Icon ? <Icon className="icon" /> : <span className="icon"></span>}
                <input name={name2} placeholder=" " type="text" required={required} onChange={onChange}></input>
                <label for={name2} className="placeholder">{placeholder2}</label>
            </div>
        </div>
    )
}

export default FullNameInput;