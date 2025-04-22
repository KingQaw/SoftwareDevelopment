import Swal from "sweetalert2";
import { useRouter } from "vue-router";
export function netError() {
    Swal.fire({
        icon: 'error',
        title: '网络错误',
        text: '请检查网络连接',
    });
}

export function tokenError() {
    Swal.fire({
        icon: 'error',
        title: '登陆状态过期',
        text: '请重新登陆',
        showConfirmButton: true,
    }).then((result) => {
        if (result.isConfirmed) {
            const router = useRouter();
            router.push('/login');
        }
    })
}