import ItemCard from "./Elements/ItemCard";

export default function SearchResultsPanel({ cards }) {
  const styling = {
    display: "flex",
    flexWrap: "wrap",
    justifyContent: "space-evenly",
    alignItems: "center",
    alignContent: "space-evenly",
    borderRadius: "0.9em",
    backgroundColor: "var(--triary)",
    margin: "2vw",
    overflowY: "auto",
    height: "80vh",
    scrollBehavior: "smooth",
    scrollbarWidth: "thin",
    scrollbarColor: "var(--accent) transparent",
  };

  return (
    cards.length > 0 && (
      <div style={styling}>
        {cards.map((card, index) => (
          <ItemCard key={index} {...card} />
        ))}
      </div>
    )
  );
}
