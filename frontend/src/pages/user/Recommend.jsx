import * as React from 'react';
import Container from "@mui/material/Container";
import LocationFinder from "../../components/user/LocationFinder";
import Map from '../../components/user/Map';
import RecommendedStores from '../../components/user/RecommendedStores';
import NotRecommendedStores from '../../components/user/NotRecommendedStores';

export default function Recommend() {
    const [coords, setCoords] = React.useState({ latitude: "", longitude: "" });
    const [stores, setStores] = React.useState([]);
    const [recommendedStores, setRecommendedStores] = React.useState([]);
    const [notRecommendedStores, setNotRecommendedStores] = React.useState([]);

    React.useEffect(() => {
        if (stores.length > 0) {
            const top5 = stores.slice(0, 5);
            const bottom5 = stores.slice(5, 10);
            setRecommendedStores(top5);
            setNotRecommendedStores(bottom5);
        }
    }, [stores]);

    return (
        <div>
            <Container component="main" maxWidth="sm">
                <LocationFinder
                    setCoords={setCoords}
                    setStores={setStores}
                    setRecommendedStores={setRecommendedStores}
                    setNotRecommendedStores={setNotRecommendedStores}
                />
            </Container>

            <Container component="main" maxWidth="lg" sx={{ marginTop: "20px" }}>
                <div style={{ display: 'flex', height: '600px' }}>
                    <div style={{ flex: 2, marginRight: '10px' }}>
                        <Map stores={stores} searchCoords={coords} />
                    </div>
                </div>
            </Container>

            <Container component="main" maxWidth="lg" sx={{ marginTop: "20px", display: 'flex', gap: '20px' }}>
                {recommendedStores.length > 0 && (
                    <div style={{ flex: 1 }}>
                        <RecommendedStores stores={recommendedStores} />
                    </div>
                )}
                {notRecommendedStores.length > 0 && (
                    <div style={{ flex: 1 }}>
                        <NotRecommendedStores stores={notRecommendedStores} />
                    </div>
                )}
            </Container>
        </div>
    );
}
