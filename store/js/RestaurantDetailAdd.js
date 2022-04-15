export default function RestaurantAddMenuPage({ $app, initialState }) {
    $app.innerHTML = ''

    this.state = initialState

    const $page = document.createElement('div')
    $page.className = 'RestaurantDetailAdd'
    $app.appendChild($page)

    this.setState = (nextState) => {
        this.state = nextState
        this.render()
    }

    this.render = () => {        
        $page.innerHTML = `
            <h3>메뉴 편집하기</h3>
            <p>메뉴명</p>
            <input id="storeName" type ='text' name='description' value="">
            <p>가격</p>
            <input id="storePrice" type ='number' name='description' value="" placeholder="숫자값">                       
            <p>이미지 주소</p>
            <input id="storeImageUrl" type ='text' name='description' value=""> 
            <div>
                <span class="add-menu">추가</span>                
                <a class="cancel-button" href=''>취소</a>          
            </div>            
        `
    }

    this.render()

    $page.addEventListener('click', (e) => {
        const updateButton = e.target.closest('.add-menu')
        if (updateButton) {
            const name = $page.querySelector('#storeName').value
            const price = $page.querySelector('#storePrice').value
            const imageUrl = $page.querySelector('#storeImageUrl').value            
            const { storeId } = this.state            

            if (name && price && imageUrl) {
                postMenu(storeId, name, price, imageUrl)
            }
        }
    })
    
    const postMenu = async (storeId, name, price, imageUrl) => {        
        const BASE_URL = 'https://dosorme.ga/api/restaurants'
        try {
            const menuData = JSON.stringify({ name: name, price: price, imageUrl: imageUrl })
            const options = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: menuData
            }            
            const response = await fetch(`${BASE_URL}/${storeId}/items`, options)
            
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