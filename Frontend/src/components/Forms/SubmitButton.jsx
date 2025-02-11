import axios from "axios";

function SubmitButton({text, data, endpoint, disabled, setVerified}){

    function PostData(){
        axios.post(endpoint, data)
        .then(response => {
            console.log('Post Created:', response.data);
            setVerified(true)
        })
        .catch(error => {
            console.error('Error creating post:', error);
            setVerified(false)
        });
    }


    return(
        <div className="submit-button">
            <button className="primary-button" 
            onClick={PostData}
            disabled={disabled}
            >{text}</button>
            
        </div>
    )
}

export default SubmitButton;