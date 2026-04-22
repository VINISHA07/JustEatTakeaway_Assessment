export function mapRestaurant(r) {
  return {
    name: r.name,
    cuisines: r.cuisines.map(c => c.name).join(', '),
    address: `${r.address.firstLine}, ${r.address.city}, ${r.address.postalCode}`,
    rating: r.rating.starRating > 0 ? `Rating: ${r.rating.starRating}` : 'No rating yet'
  };
}

async function fetchRestaurants(postcode) {
  try {
    const res = await fetch(`http://localhost:8080/api/restaurants/bypostcode/${encodeURIComponent(postcode)}`);
    if (!res.ok) throw new Error(`HTTP error: ${res.status}`);
    const data = await res.json();

    window.renderRestaurants(data.restaurants.map(mapRestaurant));

  } catch (err) {
    document.getElementById('status').textContent = `Failed to load restaurants: ${err.message}`;
  }
}

const postcode = new URLSearchParams(window.location.search).get('postcode');
fetchRestaurants(postcode);
