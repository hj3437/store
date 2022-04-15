import { request } from '/js/api.js'

export default function RestaurantListEdit({ $app, initialState }) {
    $app.innerHTML = ''

    this.state = initialState

    const $page = document.createElement('div')
    $page.className = 'RestaurantListEdit'
    $app.appendChild($page)

    this.setState = (nextState) => {
        this.state = nextState        
        this.render()
    }

    this.render = () => {        
        const {restaurant} = this.state        
        if (restaurant) {
            $page.innerHTML = `
                <h3>스토어 편집하기</h3>
                <p>스토어 이름</p>
                <input id="storeName" type ='text' name='description' value="${restaurant.name}">
                <p>주소</p>
                <input id="storeAddress" type ='text' name='description' value="${restaurant.address}">                       
                <p>이미지 </p>
                <input id="storeImageUrl" type ='text' name='description' value="${restaurant.imageUrl}">                 
                <div>
                    <span class="add-button">저장</span>
                    <a class="cancel-button"href=''>취소</a>
                </div>                      
            `
        }
    }

    this.render()

    $page.addEventListener('click', (e) => {
        const updateButton = e.target.closest('.add-button')
        if (updateButton) {
            const name = $page.querySelector('#storeName').value
            const address = $page.querySelector('#storeAddress').value
            const imageUrl = $page.querySelector('#storeImageUrl').value
            
            const { restaurantId } = this.state
            
            if (name && address && imageUrl) {                
                fetchAddMenu(restaurantId, name, address, imageUrl)
            }
        }
    })

    this.fetchRestaurants = async () => {
        try {
            const { restaurantId } = this.state
            if (restaurantId) {
                const restaurant = await request(restaurantId)
                this.setState({ ...this.state, restaurant: restaurant })
            }
        } catch (e) {
            console.log(e.message);
        }
    }

    if(this.state.restaurantId){
        this.fetchRestaurants()
    }

    const fetchAddMenu = async (storeId, name, address, imageUrl) => {
        const BASE_URL = 'https://dosorme.ga/api/restaurants'
        try {
            const data = JSON.stringify({ name: name, address: address, imageUrl: imageUrl })
            const options = {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: data
            }

            const response = await fetch(`${BASE_URL}/${storeId}`, options)

            if (response.ok) {                
                location.reload()
                return response
            }
            throw new Error('Network Error')
        } catch (e) {
            console.error(e.message);
        }
    }
}