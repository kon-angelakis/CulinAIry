import SearchResultsPanel from "../components/Home/SearchResultsPanel.jsx";
import UserPanel from "../components/Home/UserPanel.jsx";
import Footer from "../components/PageInfo/Footer.jsx";
import QueryPanel from "../components/Home/QueryPanel.jsx";

function Home() {
  let cards = [
    {
      name: "Aristofanes",
      type: "Souvlaki",
      stars: 4.7,
      reviewCount: 274,
      logo: "https://cdn.e-food.gr/shop/330532/logo?t=1647428522",
      backgroundPics: [
        "https://cdn.e-food.gr/cdn-cgi/image/f=auto/shop/330532/cover?t=1666605560&platform=web",
        "https://lh5.googleusercontent.com/p/AF1QipPW-Cb2YdUIyhGlqXaFlx-vnEGplXyurrwn10m_",
        "https://lh5.googleusercontent.com/p/AF1QipNdFm6jUoKLsgsmUaQgTiyAE10eheabUoC8-uKc",
        "https://lh5.googleusercontent.com/p/AF1QipO3bfhJ6pGlSv8dJKd2hl39NnW65WDQEkiWnfXr",
        "https://lh5.googleusercontent.com/p/AF1QipPQliaFmqyuiNa63faHhm8n5qXkojPNmuXylO-o",
      ],
    },
    {
      name: "Κεμπαπτζίδικον το γνήσιον",
      type: "Souvlaki",
      stars: 4.3,
      reviewCount: 154,
      logo: "https://cdn.e-food.gr/shop/118661/logo?t=1647428522",
      backgroundPics: [
        "https://cdn.e-food.gr/cdn-cgi/image/f=auto/shop/118661/cover?t=1647428522&platform=web",
        "https://lh5.googleusercontent.com/p/AF1QipOw_yigm01D2zpF8l0J0eKi2UAuR19x0lPGlG67",
        "https://lh5.googleusercontent.com/p/AF1QipNGG45ClvMTMK9BncVSxmXPzkTnjQGQXZtqRUWL",
        "https://lh5.googleusercontent.com/p/AF1QipOKQqy_dgI086JpLDj7BA2i1e7qK_e0sWQKpEp1",
        "https://lh5.googleusercontent.com/p/AF1QipOjACsDXZtEEc_qzXOl4mkU5cdj4J8pJFJRLrb_",
      ],
    },
    {
      name: "Coffee King",
      type: "Coffee",
      stars: 4.5,
      reviewCount: 823,
      logo: "https://cdn.e-food.gr/shop/337024/logo?t=1647428522",
      backgroundPics: [
        "https://cdn.e-food.gr/cdn-cgi/image/f=auto/shop/337024/cover?t=1728999305&platform=web",
        "https://lh5.googleusercontent.com/p/AF1QipOMwYucMomliJnZ1JYqJsvJ6BHJjHqVfqe7cz6X",
        "https://lh5.googleusercontent.com/p/AF1QipOOBF192guJkKqddtJDaUdH8875kO9kuYTmUzSt",
        "https://lh5.googleusercontent.com/p/AF1QipMXC8xZqcYVGtYFISWguOtE5Xs7sUcUbybtnNAP",
        "https://lh5.googleusercontent.com/p/AF1QipOd3_Q1xNAcFN43zS8mBoF55Wl5LG29r8w07ozl",
      ],
    },
    {
      name: "Coffee King",
      type: "Coffee",
      stars: 4.5,
      reviewCount: 823,
      logo: "https://cdn.e-food.gr/shop/337024/logo?t=1647428522",
      backgroundPics: [
        "https://cdn.e-food.gr/cdn-cgi/image/f=auto/shop/337024/cover?t=1728999305&platform=web",
        "https://lh5.googleusercontent.com/p/AF1QipOMwYucMomliJnZ1JYqJsvJ6BHJjHqVfqe7cz6X",
        "https://lh5.googleusercontent.com/p/AF1QipOOBF192guJkKqddtJDaUdH8875kO9kuYTmUzSt",
        "https://lh5.googleusercontent.com/p/AF1QipMXC8xZqcYVGtYFISWguOtE5Xs7sUcUbybtnNAP",
        "https://lh5.googleusercontent.com/p/AF1QipOd3_Q1xNAcFN43zS8mBoF55Wl5LG29r8w07ozl",
      ],
    },
    {
      name: "Il Toto",
      type: "Coffee",
      stars: 4.6,
      reviewCount: 152,
      logo: "https://cdn.e-food.gr/shop/7103302/logo?t=1712923149",
      backgroundPics: [
        "https://cdn.e-food.gr/cdn-cgi/image/f=auto/shop/7103302/cover?t=1712923149&platform=web",
        "https://lh5.googleusercontent.com/p/AF1QipPOz_kUPmvmMIllvPrH19ABrGCARNXJr5seslQ",
        "https://lh3.googleusercontent.com/gps-proxy/ALd4DhEvXWHe3q0S9DvI3VAvhUJy2x7PDlvyaWtepSuy2KmB-6NoEtc7yYAvc-YNOog3HDw2fUtf7_Xdkw7ipX6hVxp1Yab6zp6PzrqFRv9cMoNz2wtcTuuk-6gZHnWTsVsEpukDQ9IpOR4uQEdLMWNPmxvVIfnrdyawfxHRqn_ZE5CbKKQk5bhEDZaC",
        "https://lh5.googleusercontent.com/p/AF1QipOWJlM1kU9ACkZH6DLRQUk4sfodyGfqDmB9LcaV",
      ],
    },
  ];
  // cards = [];
  return (
    <>
      <div
        className="page-container"
        style={{
          height: "100vh",
          backgroundColor: "var(--tetriary)",
          borderRadius: ".8em",
        }}
      >
        <UserPanel
          height={"10vh"}
          name={JSON.parse(localStorage.getItem("UserDetails")).firstName}
          pfp={
            JSON.parse(localStorage.getItem("UserDetails")).pfp ||
            "https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_1280.png"
          }
        />
        <QueryPanel />
        <SearchResultsPanel cards={cards} />
      </div>

      <Footer />
    </>
  );
}

export default Home;
