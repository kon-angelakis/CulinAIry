function Home() {
  return (
    <>
      <p>Welcome {JSON.parse(localStorage.getItem("UserDetails")).email}</p>
      <img
        style={{
          borderRadius: "100%",
          objectFit: "cover",
          height: "50px",
          width: "50px",
        }}
        src={
          JSON.parse(localStorage.getItem("UserDetails")).pfp ||
          "https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_1280.png"
        }
      ></img>
    </>
  );
}

export default Home;
