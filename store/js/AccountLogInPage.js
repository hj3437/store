export default function AccountLogInPage({ $app, display }) {
    this.state = display

    const $page = document.createElement('div')
    $page.className = 'LogInPage'
    $app.appendChild($page)

    this.setState = (nextState) => {
        this.state = nextState
        $page.remove()
        this.render()
    }

    this.render = () => {                
        $page.innerHTML = `
        <div class="modal ${!this.state ? 'hidden' : ''}">
            <div class="modal-overlay"></div>
            <div class="modal-content">
                <h3>
                    <span class="material-icons-outlined">face</span>
                    스토어 로그인
                </h3>                
                <div>
                    <div>
                        <input id="login-user-id" type ='text' name='id' placeholder='아이디' autocomplete='off' value="admin">                    
                    </div>
                    <div>
                        <input id="login-user-password" type ='password' name='id' placeholder='비밀번호' autocomplete='off' value="">
                    </div>                    
                </form>
                <div class="login-option-buttons">                    
                    <span class="login-submit">로그인</span>
                    <p class="login-close-button">닫기</p>
                </div>
            </div>
        <div>
        `
    }

    this.render()

    $page.addEventListener('click', async (e) => {
        const modal = $page.querySelector('.modal')

        if (e.target.closest('.login-submit')) {
            const userId = $page.querySelector('#login-user-id').value
            const userPassword = $page.querySelector('#login-user-password').value

            const result = await checkUser(userId, userPassword)
            if (result) {
                sessionStorage.setItem("store-admin", true)

                if (sessionStorage.getItem("store-admin")){
                    this.setState(false)                                      
                    alert('로그인 성공')
                    location.reload()
                }
            } else {
                alert('로그인 에러가 발생하였습니다.')
            }

            return
        }

        if (e.target.closest('.login-close-button')) {
            modal.classList.add('hidden')
            this.setState(false)
            return
        }

        if (e.target.closest('.modal-overlay')) {
            modal.classList.add('hidden')
            this.setState(false)
            return
        }
    })

    const checkUser = async (userId, userPassword) => {
        const BASE_URL = 'https://dosorme.ga/api/user/'
        try {
            const userData = JSON.stringify({ id: userId, password: userPassword })
            const options = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: userData
            }
            const response = await fetch(BASE_URL, options)
            if (response.ok) {           
                return response.json()
            }
            throw new Error('Network Error')
        } catch (e) {
            console.error(e.message);
        }
    }
}